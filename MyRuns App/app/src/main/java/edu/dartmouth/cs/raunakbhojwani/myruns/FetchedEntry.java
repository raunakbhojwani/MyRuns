package edu.dartmouth.cs.raunakbhojwani.myruns;

/**
 * Helper to be used in HistoryFragment, holds the title and summar
 * of each Exercise Entry object to be displayed in a ListView
 *
 * Created by RaunakBhojwani on 2/1/17.
 * @author RaunakBhojwani
 */

public class FetchedEntry {
    private String entryHeader;
    private String entrySummary;

    public FetchedEntry(String header, String summary) {
        this.entryHeader = header;
        this.entrySummary = summary;
    }

    //getters and setters
    public String getHeader() {
        return entryHeader;
    }

    public void setEntryHeader(String header) {
        this.entryHeader = header;
    }

    public String getSummary() {
        return entrySummary;
    }

    public void setEntrySummary(String summary) {
        this.entrySummary = summary;
    }
}
