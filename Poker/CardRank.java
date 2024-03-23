public enum CardRank {
    TWO {
        public String toString() {
            return "2";
        }

        public int value() {
            return 2;
        }
    },
    THREE {
        public String toString() {
            return "3";
        }

        public int value() {
            return 3;
        }
    },
    FOUR {
        public String toString() {
            return "4";
        }

        public int value() {
            return 4;
        }
    },
    FIVE {
        public String toString() {
            return "5";
        }

        public int value() {
            return 5;
        }
    },
    SIX {
        public String toString() {
            return "6";
        }

        public int value() {
            return 6;
        }
    },
    SEVEN {
        public String toString() {
            return "7";
        }

        public int value() {
            return 7;
        }
    },
    EIGHT {
        public String toString() {
            return "8";
        }

        public int value() {
            return 8;
        }
    },
    NINE {
        public String toString() {
            return "9";
        }

        public int value() {
            return 9;
        }
    },
    TEN {
        public String toString() {
            return "10";
        }

        public int value() {
            return 10;
        }
    },
    JACK {
        public String toString() {
            return "J";
        }

        public int value() {
            return 11;
        }
    },
    QUEEN {
        public String toString() {
            return "Q";
        }

        public int value() {
            return 12;
        }
    },
    KING {
        public String toString() {
            return "K";
        }

        public int value() {
            return 13;
        }
    },
    ACE {
        public String toString() {
            return "A";
        }

        public int value() {
            return 14;
        }
    };

    public abstract int value();
}