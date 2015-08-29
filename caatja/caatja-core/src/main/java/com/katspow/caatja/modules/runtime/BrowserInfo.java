package com.katspow.caatja.modules.runtime;

import java.util.ArrayList;
import java.util.List;

import com.katspow.caatja.core.Caatja;

public class BrowserInfo {

    public String browser;
    public String version;
    public String OS;
    
    // TODO window.devicePixelRatio
    public static double devicePixelRatio = 1; 

    public List<DataBrowserData> dataBrowser;
    public List<DataBrowserData> dataOS;

    public String versionSearchString;

    private class DataBrowserData {
        String string;
        String subString;
        String identity;
        String versionSearch;
        String prop;

        DataBrowserData() {
            init();
        }

        void init() {
        };

    }

    private void initBrowserData() {
        dataBrowser = new ArrayList<DataBrowserData>() {
            {
                add(new DataBrowserData() {
                    void init() {
                        string = Caatja.getUserAgent();
                        subString = "Chrome";
                        identity = "Chrome";
                    }
                });

                add(new DataBrowserData() {
                    void init() {
                        string = Caatja.getUserAgent();
                        subString = "OmniWeb";
                        versionSearch = "OmniWeb/";
                        identity = "OmniWeb";
                    }
                });

                add(new DataBrowserData() {
                    void init() {
                        string = Caatja.getUserAgent();
                        subString = "Apple";
                        identity = "Safari";
                        versionSearch = "Version";
                    }
                });

                add(new DataBrowserData() {
                    void init() {
                        // FIXME how to do "window.opera" ?
                        prop = null;
                        identity = "Opera";
                    }
                });

                add(new DataBrowserData() {
                    void init() {
                        // FIXME how to do "Caatja.vendor" ?
                        string = null;
                        subString = "iCab";
                        identity = "iCab";
                    }
                });

                add(new DataBrowserData() {
                    void init() {
                        // FIXME how to do "Caatja.vendor" ?
                        string = null;
                        subString = "KDE";
                        identity = "Konqueror";
                    }
                });

                add(new DataBrowserData() {
                    void init() {
                        string = Caatja.getUserAgent();
                        subString = "Firefox";
                        identity = "Firefox";
                    }
                });

                add(new DataBrowserData() {
                    void init() {
                        // FIXME how to do "Caatja.vendor" ?
                        string = null;
                        subString = "Camino";
                        identity = "Camino";
                    }
                });

                // for newer Netscapes (6+)
                add(new DataBrowserData() {
                    void init() {
                        string = Caatja.getUserAgent();
                        subString = "Netscape";
                        identity = "Netscape";
                    }
                });

                add(new DataBrowserData() {
                    void init() {
                        string = Caatja.getUserAgent();
                        subString = "MSIE";
                        identity = "Explorer";
                        versionSearch = "MSIE";
                    }
                });

                add(new DataBrowserData() {
                    void init() {
                        string = Caatja.getUserAgent();
                        subString = "Explorer";
                        identity = "Explorer";
                        versionSearch = "Explorer";
                    }
                });

                add(new DataBrowserData() {
                    void init() {
                        string = Caatja.getUserAgent();
                        subString = "Gecko";
                        identity = "Mozilla";
                        versionSearch = "rv";
                    }
                });

                // for older Netscapes (4-)
                add(new DataBrowserData() {
                    void init() {
                        string = Caatja.getUserAgent();
                        subString = "Mozilla";
                        identity = "Netscape";
                        versionSearch = "Mozilla";
                    }
                });

            }
        };
    }

    private void initOSData() {
        dataOS = new ArrayList<DataBrowserData>() {
            {

                add(new DataBrowserData() {
                    void init() {
                        string = Caatja.getPlatform();
                        subString = "Win";
                        identity = "Windows";
                    }
                });

                add(new DataBrowserData() {
                    void init() {
                        string = Caatja.getPlatform();
                        subString = "Mac";
                        identity = "Mac";
                    }
                });

                add(new DataBrowserData() {
                    void init() {
                        string = Caatja.getUserAgent();
                        subString = "iPhone";
                        identity = "iPhone/iPod";
                    }
                });

                add(new DataBrowserData() {
                    void init() {
                        string = Caatja.getPlatform();
                        subString = "Linux";
                        identity = "Linux";
                    }
                });

            }
        };
    }

    public void init() {
        // Add by me
        initBrowserData();
        initOSData();

        String searchString = this.searchString(this.dataBrowser);
        if (searchString == null) {
            this.browser = "An unknown browser";
        } else {
            this.browser = searchString;
        }

        String userAgent = this.searchVersion(Caatja.getUserAgent());
        String appVersion = this.searchVersion(Caatja.getAppVersion());

        if (userAgent == null) {
            if (appVersion == null) {
                this.version = "an unknown version";

            } else {
                this.version = appVersion;
            }

        } else {
            this.version = userAgent;
        }

        String dataOS = this.searchString(this.dataOS);
        if (dataOS == null) {
            this.OS = "an unknown OS";
        } else {
            this.OS = dataOS;
        }

    }

    public String searchString(List<DataBrowserData> data) {

        for (DataBrowserData dataBrowserData : data) {
            String dataString = dataBrowserData.string;
            String dataProp = dataBrowserData.prop;

            String versionSearch = dataBrowserData.versionSearch;
            if (versionSearch == null) {
                this.versionSearchString = dataBrowserData.identity;
            } else {
                this.versionSearchString = versionSearch;
            }

            if (dataString != null) {
                if (dataString.contains(dataBrowserData.subString)) {
                    return dataBrowserData.identity;
                }

            } else if (dataProp != null) {
                return dataBrowserData.identity;
            }

        }

        // Should never be reached !
        return null;
    }

    public String searchVersion(String dataString) {
        
        if (dataString == null) {
            return null;
        }
        
        int index = dataString.indexOf(this.versionSearchString);
        if (index == -1)
            return null;
        return dataString.substring(index + this.versionSearchString.length() + 1);
    }
    
    public BrowserInfo() {
        this.init();
    }

}
