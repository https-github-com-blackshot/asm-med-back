package kz.beeset.med.admin.utils.validators;

public class SFunctions {

    private static Boolean Glasn(String stc) {
        return "АЕИОУЫЭЮЯЁаеиоуыэюяё".contains(stc);
    }

    private static String R2E(String c) {
        String vRes;
        Integer i1;
        vRes = c;
        i1 = "АБВГДЕЗИЙКЛМНОПРСТУФЫЭабвгдезийклмнопрстуфыэ".indexOf(c);
        if (i1 >= 0) {
            vRes = "ABVGDEZIIKLMNOPRSTUFYEabvgdeziiklmnoprstufye".substring(i1, i1 + 1);
        } else {
            if (c.equals("Ё")) {
                vRes = "Yo";
            } else if (c.equals("Ж")) {
                vRes = "Zh";
            } else if (c.equals("Х")) {
                vRes = "Kh";
            } else if (c.equals("Ц")) {
                vRes = "Ts";
            } else if (c.equals("Ч")) {
                vRes = "Ch";
            } else if (c.equals("Ш")) {
                vRes = "Sh";
            } else if (c.equals("Щ")) {
                vRes = "Chsh";
            } else if (c.equals("Ъ")) {
                vRes = "";
            } else if (c.equals("Ь")) {
                vRes = "";
            } else if (c.equals("Ю")) {
                vRes = "Yu";
            } else if (c.equals("Я")) {
                vRes = "Ya";
            } else if (c.equals("ж")) {
                vRes = "zh";
            } else if (c.equals("х")) {
                vRes = "kh";
            } else if (c.equals("ц")) {
                vRes = "ts";
            } else if (c.equals("ч")) {
                vRes = "ch";
            } else if (c.equals("ш")) {
                vRes = "sh";
            } else if (c.equals("щ")) {
                vRes = "chsh";
            } else if (c.equals("ъ")) {
                vRes = "";
            } else if (c.equals("ь")) {
                vRes = "";
            } else if (c.equals("ю")) {
                vRes = "yu";
            } else if (c.equals("я")) {
                vRes = "ya";
            } else if (c.equals("ё")) {
                vRes = "yo";
            }
        }
        return vRes;
    }

    public static String translit(String s) {

        //s = s.toUpperCase();
        String ss;
        Integer ii;
        Integer i;
        Boolean b;

        ss = "";
        ii = s.length() - 1;
        i = 0;
        if (s.trim().length() > 0) {
            while (true) {
                b = false;
                if (i > 0) {

                    if ((i < ii) && (s.substring(i, i + 1).equals("С")) && Glasn(s.substring(i - 1, i)) && Glasn(s.substring(i + 1, i + 2))) {
                        b = true;
                        ss = ss.concat("Ss");
                    }
                    if ((i < ii) && (s.substring(i, i + 1).equals("с")) && Glasn(s.substring(i - 1, i)) && Glasn(s.substring(i + 1, i + 2))) {
                        b = true;
                        ss = ss.concat("ss");
                    }


                    if (s.substring(i, i + 1).equals("е") && (s.substring(i - 1, i).equals("ь") || Glasn(s.substring(i - 1, i)))) {
                        b = true;
                        ss = ss.concat("ye");
                    }

                    if ((i + 1 < ii)
                            && s.substring(i, i + 1).equals("С")
                            && s.substring(i + 1, i + 2).equals("С")
                            && (!Glasn(s.substring(i + 2, i + 3)))) {
                        b = true;
                        ss = ss.concat("S");
                        i = i + 1;
                    }
                    if ((i + 1 < ii)
                            && s.substring(i, i + 1).equals("с")
                            && s.substring(i + 1, i + 2).equals("с")
                            && (!Glasn(s.substring(i + 2, i + 3)))) {
                        b = true;
                        ss = ss.concat("s");
                        i = i + 1;
                    }
                }
                if ((i.equals(ii)) && (s.substring(i, i + 1).equals("Й"))) {
                    b = true;
                    ss = ss.concat("Y");
                };
                if ((i.equals(ii)) && (s.substring(i, i + 1).equals("й"))) {
                    b = true;
                    ss = ss.concat("y");
                };
                if ((i.equals(ii)) && (s.substring(i, i + 1).equals("Е"))) {
                    b = true;
                    ss = ss.concat("Ye");
                }
                if ((i.equals(ii)) && (s.substring(i, i + 1).equals("е"))) {
                    b = true;
                    ss = ss.concat("ye");
                }
                if (s.substring(i, i + 1).equals("К") && (s.length() > i + 2) && (s.substring(i + 1, i + 2).equals("с"))) {
                    b = true;
                    ss = ss.concat("X");
                    i = i + 1;
                }
                if (s.substring(i, i + 1).equals("к") && (s.length() > i + 2) && (s.substring(i + 1, i + 2).equals("с"))) {
                    b = true;
                    ss = ss.concat("x");
                    i = i + 1;
                }
                if (s.substring(i, i + 1).equals("Д") && (s.length() > i + 2) && (s.substring(i + 1, i + 2).equals("ж"))) {
                    b = true;
                    ss = ss.concat("J");
                    i = i + 1;
                }
                if (s.substring(i, i + 1).equals("д") && (s.length() > i + 2) && (s.substring(i + 1, i + 2).equals("ж"))) {
                    b = true;
                    ss = ss.concat("j");
                    i = i + 1;
                }
                if (!b) {
                    ss = ss.concat(R2E(s.substring(i, i + 1)));
                }
                i = i + 1;
                if (i > ii) {
                    break;
                }
            }
        }
        return ss;
    }
}
