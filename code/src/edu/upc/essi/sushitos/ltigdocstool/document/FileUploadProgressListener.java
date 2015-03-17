package edu.upc.essi.sushitos.ltigdocstool.document;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

import com.google.common.collect.Maps;
import com.google.gdata.client.media.ResumableGDataFileUploader;
import com.google.gdata.client.uploader.FileUploadData;
import com.google.gdata.client.uploader.ProgressListener;
import com.google.gdata.client.uploader.ResumableHttpFileUploader;
import com.google.gdata.data.docs.DocumentListEntry;
import com.google.gdata.data.media.MediaFileSource;
import com.google.gdata.util.ServiceException;

public class FileUploadProgressListener implements ProgressListener {

    private static final int NUM_RETRIES = 3;

    private static final long SLEEP_MILL_SECOND = 100;

    Logger logger = Logger.getLogger(this.getClass().getName());

    private Map<String, ResumableGDataFileUploader> trackedUploaders = Maps.newHashMap();
    private int pendingRequests;
    private Map<String, DocumentListEntry> uploaded = Maps.newHashMap();
    private Map<String, MediaFileSource> uploadMedia = Maps.newHashMap();
    private int count = 0;
    private boolean isUploadedEntry;

    // boolean processed;

    public FileUploadProgressListener() {
        this(true);
    }

    public FileUploadProgressListener(boolean isUploadedEntry) {
        this.pendingRequests = 0;
        this.isUploadedEntry = isUploadedEntry;
    }

    public synchronized void listenTo(ResumableGDataFileUploader uploader, String key, MediaFileSource media) {
        if (!trackedUploaders.containsKey(key)) {
            this.trackedUploaders.put(key, uploader);
            this.uploadMedia.put(key, media);
        } else {
            String key2 = key + "-" + String.valueOf(++count);
            while (trackedUploaders.containsKey(key2)) {
                key2 = key + "-" + String.valueOf(++count);
            }
            this.trackedUploaders.put(key2, uploader);
            this.uploadMedia.put(key2, media);
        }
        this.pendingRequests++;
    }

    public synchronized void progressChanged(ResumableHttpFileUploader uploader) {
        String fileId = ((FileUploadData) uploader.getData()).getFileName();
        switch (uploader.getUploadState()) {
            case COMPLETE:
            case CLIENT_ERROR:
                pendingRequests -= 1;
                // output.println(fileId + ": Completed");
                logger.info(fileId + ": Completed");
                break;
            case IN_PROGRESS:
                // output.println(fileId + ":" + String.format("%3.0f",
                // uploader.getProgress() * 100) + "%");
                logger.info(fileId + ":" + String.format("%3.0f", uploader.getProgress() * 100) + "%");
                break;
            case NOT_STARTED:
                // output.println(fileId + ":" + "Not Started");
                logger.info(fileId + ":" + "Not Started");
                break;
        }
    }

    public synchronized boolean isDone() {
        // process all responses
        boolean isDone = true;
        List<String> doneList = new ArrayList<String>();
        for (Map.Entry<String, ResumableGDataFileUploader> mapEntry : trackedUploaders.entrySet()) {
            String key = mapEntry.getKey();
            ResumableGDataFileUploader uploader = mapEntry.getValue();

            if (!uploader.isDone()) {
                isDone = false;
            } else {
                switch (uploader.getUploadState()) {
                    case COMPLETE:
                        for (int r = 0; r < NUM_RETRIES; r++) {
                            try {
                                if (isUploadedEntry) {
                                    DocumentListEntry entry =
                                                                    uploader.getResponse(DocumentListEntry.class);
                                    uploaded.put(key, entry);
                                } else {
                                    uploaded.put(key, null);
                                }
                                break;

                            } catch (IOException e) {
                                if (r == (NUM_RETRIES - 1)) {
                                    // failed.put(key,
                                    // "Upload completed, but unexpected error "
                                    // + "reading server response");
                                    logger.warning("Upload completed, but unexpected error "
                                                                            + "reading server response. key = " + key);
                                }
                                sleep(SLEEP_MILL_SECOND);
                            } catch (ServiceException e) {
                                if (r == (NUM_RETRIES - 1)) {
                                    // failed.put(key,
                                    // "Upload completed, but failed to parse server response");
                                    logger.warning("Upload completed, but failed to parse server response. key = " + key);
                                }
                                sleep(SLEEP_MILL_SECOND);
                            }
                        }
                        break;
                    case CLIENT_ERROR:
                        // failed.put(key, "Failed at " +
                        // uploader.getProgress());
                        logger.warning("Failed at " + uploader.getProgress() + ". key = " + key);
                        break;
                }
                doneList.add(key);
            }
        }

        for (String key : doneList) {
            trackedUploaders.remove(key);
        }

        if (!isDone) {
            return false;
        }

        // not done if there are any pending requests.
        if (pendingRequests > 0) {
            return false;
        }
        // if all responses are processed., nothing to do.
        /*
         * if (processed) { return true; }
         */
        // check if all response streams are available.
        /*
         * for (ResumableGDataFileUploader uploader : trackedUploaders.values())
         * { if (!uploader.isDone()) { return false; } }
         */

        // processed = true;
        // output.println("All requests done");
        // logger.info("All requests done");
        return true;
    }

    public synchronized Collection<DocumentListEntry> getUploaded() {
        if (!isDone()) {
            return null;
        }
        return uploaded.values();
    }

    public synchronized Map<String, MediaFileSource> getUploadMediaMap() {
        return uploadMedia;
    }

    public synchronized void sleep(long msec) {
        try {
            wait(msec);
        } catch (InterruptedException e) {
        }
    }

    public synchronized Map<String, DocumentListEntry> getUploadedMap() {
        isDone();
        return uploaded;
    }

}
