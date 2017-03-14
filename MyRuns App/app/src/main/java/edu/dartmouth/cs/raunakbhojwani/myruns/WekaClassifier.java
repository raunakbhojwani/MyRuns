package edu.dartmouth.cs.raunakbhojwani.myruns;

/**
 * Created by RaunakBhojwani on 2/15/17.
 */

class WekaClassifier {

    public static double classify(Object[] i)
            throws Exception {

        double p = Double.NaN;
        p = WekaClassifier.N7257442a0(i);
        return p;
    }
    static double N7257442a0(Object []i) {
        double p = Double.NaN;
        if (i[0] == null) {
            p = 0;
        } else if (((Double) i[0]).doubleValue() <= 158.501333) {
            p = WekaClassifier.N6bd744681(i);
        } else if (((Double) i[0]).doubleValue() > 158.501333) {
            p = WekaClassifier.N6ef606538(i);
        }
        return p;
    }
    static double N6bd744681(Object []i) {
        double p = Double.NaN;
        if (i[64] == null) {
            p = 0;
        } else if (((Double) i[64]).doubleValue() <= 3.300554) {
            p = 0;
        } else if (((Double) i[64]).doubleValue() > 3.300554) {
            p = WekaClassifier.N27b030352(i);
        }
        return p;
    }
    static double N27b030352(Object []i) {
        double p = Double.NaN;
        if (i[0] == null) {
            p = 0;
        } else if (((Double) i[0]).doubleValue() <= 130.414173) {
            p = WekaClassifier.N6a0479093(i);
        } else if (((Double) i[0]).doubleValue() > 130.414173) {
            p = WekaClassifier.N223a06774(i);
        }
        return p;
    }
    static double N6a0479093(Object []i) {
        double p = Double.NaN;
        if (i[21] == null) {
            p = 1;
        } else if (((Double) i[21]).doubleValue() <= 0.216353) {
            p = 1;
        } else if (((Double) i[21]).doubleValue() > 0.216353) {
            p = 0;
        }
        return p;
    }
    static double N223a06774(Object []i) {
        double p = Double.NaN;
        if (i[0] == null) {
            p = 1;
        } else if (((Double) i[0]).doubleValue() <= 146.085356) {
            p = WekaClassifier.N739fe25c5(i);
        } else if (((Double) i[0]).doubleValue() > 146.085356) {
            p = WekaClassifier.N558855826(i);
        }
        return p;
    }
    static double N739fe25c5(Object []i) {
        double p = Double.NaN;
        if (i[7] == null) {
            p = 0;
        } else if (((Double) i[7]).doubleValue() <= 2.216433) {
            p = 0;
        } else if (((Double) i[7]).doubleValue() > 2.216433) {
            p = 1;
        }
        return p;
    }
    static double N558855826(Object []i) {
        double p = Double.NaN;
        if (i[2] == null) {
            p = 0;
        } else if (((Double) i[2]).doubleValue() <= 34.257169) {
            p = WekaClassifier.N1c8db20b7(i);
        } else if (((Double) i[2]).doubleValue() > 34.257169) {
            p = 1;
        }
        return p;
    }
    static double N1c8db20b7(Object []i) {
        double p = Double.NaN;
        if (i[19] == null) {
            p = 1;
        } else if (((Double) i[19]).doubleValue() <= 0.322823) {
            p = 1;
        } else if (((Double) i[19]).doubleValue() > 0.322823) {
            p = 0;
        }
        return p;
    }
    static double N6ef606538(Object []i) {
        double p = Double.NaN;
        if (i[64] == null) {
            p = 1;
        } else if (((Double) i[64]).doubleValue() <= 22.590961) {
            p = WekaClassifier.N627784d39(i);
        } else if (((Double) i[64]).doubleValue() > 22.590961) {
            p = WekaClassifier.N4263d24528(i);
        }
        return p;
    }
    static double N627784d39(Object []i) {
        double p = Double.NaN;
        if (i[2] == null) {
            p = 1;
        } else if (((Double) i[2]).doubleValue() <= 101.857023) {
            p = WekaClassifier.N75c4e95a10(i);
        } else if (((Double) i[2]).doubleValue() > 101.857023) {
            p = WekaClassifier.N29a76aea24(i);
        }
        return p;
    }
    static double N75c4e95a10(Object []i) {
        double p = Double.NaN;
        if (i[0] == null) {
            p = 1;
        } else if (((Double) i[0]).doubleValue() <= 185.537076) {
            p = WekaClassifier.N4a8000311(i);
        } else if (((Double) i[0]).doubleValue() > 185.537076) {
            p = WekaClassifier.N43d0cc213(i);
        }
        return p;
    }
    static double N4a8000311(Object []i) {
        double p = Double.NaN;
        if (i[7] == null) {
            p = 1;
        } else if (((Double) i[7]).doubleValue() <= 6.790644) {
            p = WekaClassifier.N5ab24a5b12(i);
        } else if (((Double) i[7]).doubleValue() > 6.790644) {
            p = 0;
        }
        return p;
    }
    static double N5ab24a5b12(Object []i) {
        double p = Double.NaN;
        if (i[64] == null) {
            p = 1;
        } else if (((Double) i[64]).doubleValue() <= 6.733428) {
            p = 1;
        } else if (((Double) i[64]).doubleValue() > 6.733428) {
            p = 0;
        }
        return p;
    }
    static double N43d0cc213(Object []i) {
        double p = Double.NaN;
        if (i[0] == null) {
            p = 1;
        } else if (((Double) i[0]).doubleValue() <= 367.848043) {
            p = WekaClassifier.N5661ab2614(i);
        } else if (((Double) i[0]).doubleValue() > 367.848043) {
            p = WekaClassifier.N20d88ab222(i);
        }
        return p;
    }
    static double N5661ab2614(Object []i) {
        double p = Double.NaN;
        if (i[4] == null) {
            p = 1;
        } else if (((Double) i[4]).doubleValue() <= 18.873999) {
            p = WekaClassifier.N120d11f115(i);
        } else if (((Double) i[4]).doubleValue() > 18.873999) {
            p = WekaClassifier.N6d71989c18(i);
        }
        return p;
    }
    static double N120d11f115(Object []i) {
        double p = Double.NaN;
        if (i[6] == null) {
            p = 1;
        } else if (((Double) i[6]).doubleValue() <= 10.156287) {
            p = 1;
        } else if (((Double) i[6]).doubleValue() > 10.156287) {
            p = WekaClassifier.N5a25717916(i);
        }
        return p;
    }
    static double N5a25717916(Object []i) {
        double p = Double.NaN;
        if (i[64] == null) {
            p = 2;
        } else if (((Double) i[64]).doubleValue() <= 8.210605) {
            p = WekaClassifier.N36123de317(i);
        } else if (((Double) i[64]).doubleValue() > 8.210605) {
            p = 1;
        }
        return p;
    }
    static double N36123de317(Object []i) {
        double p = Double.NaN;
        if (i[3] == null) {
            p = 2;
        } else if (((Double) i[3]).doubleValue() <= 28.886838) {
            p = 2;
        } else if (((Double) i[3]).doubleValue() > 28.886838) {
            p = 0;
        }
        return p;
    }
    static double N6d71989c18(Object []i) {
        double p = Double.NaN;
        if (i[24] == null) {
            p = 1;
        } else if (((Double) i[24]).doubleValue() <= 2.747677) {
            p = WekaClassifier.N17098e5a19(i);
        } else if (((Double) i[24]).doubleValue() > 2.747677) {
            p = 1;
        }
        return p;
    }
    static double N17098e5a19(Object []i) {
        double p = Double.NaN;
        if (i[9] == null) {
            p = 1;
        } else if (((Double) i[9]).doubleValue() <= 2.957237) {
            p = 1;
        } else if (((Double) i[9]).doubleValue() > 2.957237) {
            p = WekaClassifier.N66d68b7b20(i);
        }
        return p;
    }
    static double N66d68b7b20(Object []i) {
        double p = Double.NaN;
        if (i[0] == null) {
            p = 1;
        } else if (((Double) i[0]).doubleValue() <= 298.153425) {
            p = WekaClassifier.N418c4f6721(i);
        } else if (((Double) i[0]).doubleValue() > 298.153425) {
            p = 2;
        }
        return p;
    }
    static double N418c4f6721(Object []i) {
        double p = Double.NaN;
        if (i[7] == null) {
            p = 2;
        } else if (((Double) i[7]).doubleValue() <= 5.536867) {
            p = 2;
        } else if (((Double) i[7]).doubleValue() > 5.536867) {
            p = 1;
        }
        return p;
    }
    static double N20d88ab222(Object []i) {
        double p = Double.NaN;
        if (i[5] == null) {
            p = 1;
        } else if (((Double) i[5]).doubleValue() <= 23.371628) {
            p = 1;
        } else if (((Double) i[5]).doubleValue() > 23.371628) {
            p = WekaClassifier.N7b78bbad23(i);
        }
        return p;
    }
    static double N7b78bbad23(Object []i) {
        double p = Double.NaN;
        if (i[9] == null) {
            p = 2;
        } else if (((Double) i[9]).doubleValue() <= 6.082469) {
            p = 2;
        } else if (((Double) i[9]).doubleValue() > 6.082469) {
            p = 1;
        }
        return p;
    }
    static double N29a76aea24(Object []i) {
        double p = Double.NaN;
        if (i[0] == null) {
            p = 1;
        } else if (((Double) i[0]).doubleValue() <= 733.153899) {
            p = WekaClassifier.Na73efce25(i);
        } else if (((Double) i[0]).doubleValue() > 733.153899) {
            p = 2;
        }
        return p;
    }
    static double Na73efce25(Object []i) {
        double p = Double.NaN;
        if (i[16] == null) {
            p = 2;
        } else if (((Double) i[16]).doubleValue() <= 1.90671) {
            p = 2;
        } else if (((Double) i[16]).doubleValue() > 1.90671) {
            p = WekaClassifier.N6e9a792826(i);
        }
        return p;
    }
    static double N6e9a792826(Object []i) {
        double p = Double.NaN;
        if (i[5] == null) {
            p = 1;
        } else if (((Double) i[5]).doubleValue() <= 21.311896) {
            p = 1;
        } else if (((Double) i[5]).doubleValue() > 21.311896) {
            p = WekaClassifier.N5081028527(i);
        }
        return p;
    }
    static double N5081028527(Object []i) {
        double p = Double.NaN;
        if (i[3] == null) {
            p = 2;
        } else if (((Double) i[3]).doubleValue() <= 48.263159) {
            p = 2;
        } else if (((Double) i[3]).doubleValue() > 48.263159) {
            p = 1;
        }
        return p;
    }
    static double N4263d24528(Object []i) {
        double p = Double.NaN;
        if (i[64] == null) {
            p = 2;
        } else if (((Double) i[64]).doubleValue() <= 28.94938) {
            p = WekaClassifier.N57fb585829(i);
        } else if (((Double) i[64]).doubleValue() > 28.94938) {
            p = 2;
        }
        return p;
    }
    static double N57fb585829(Object []i) {
        double p = Double.NaN;
        if (i[3] == null) {
            p = 1;
        } else if (((Double) i[3]).doubleValue() <= 18.627927) {
            p = 1;
        } else if (((Double) i[3]).doubleValue() > 18.627927) {
            p = WekaClassifier.N6a1538b630(i);
        }
        return p;
    }
    static double N6a1538b630(Object []i) {
        double p = Double.NaN;
        if (i[32] == null) {
            p = 2;
        } else if (((Double) i[32]).doubleValue() <= 8.547489) {
            p = 2;
        } else if (((Double) i[32]).doubleValue() > 8.547489) {
            p = 1;
        }
        return p;
    }
}

