package com.subject7.samples.reporting.domain;

public enum BrowserType {
    CHROME("Chrome"),
    FIRE_FOX("Firefox"),
    EDGE("Edge"),
    IE("Internet Explorer"),
    SAFARI("Safari"),
    HEADLESS("Headless");

    private String caption;

    private BrowserType(String caption) {
        this.caption = caption;
    }

    public String getCaption() {
        return this.caption;
    }

    public static BrowserType getBrowserTypeByCaption(String capitalizedBrowserName) {
        if (capitalizedBrowserName != null) {
            BrowserType[] var1 = values();
            int var2 = var1.length;

            for(int var3 = 0; var3 < var2; ++var3) {
                BrowserType b = var1[var3];
                if (capitalizedBrowserName.equalsIgnoreCase(b.getCaption())) {
                    return b;
                }
            }
        }

        throw new IllegalArgumentException("No browser type with name " + capitalizedBrowserName);
    }
}
