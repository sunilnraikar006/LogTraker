package com.sgs.mylibrary.util;


import okhttp3.FormBody;
import okhttp3.HttpUrl;
import okhttp3.RequestBody;

/**
 * LibConstants are stored here
 */
public final class Constant {


    public final static String DEFAULT_SDK_VERSION="1.x.x";
    public final static String DEFAULT_APP_VERSION="1.1.x";
    public final static String SDK_MODE = "DEV";
    public static final String BASE_URL = "http://loganalyticssession.shrigowri.com:5551/";
//    public static final String BASE_URL = "http://192.168.0.29:5002/";
    public static final String ConfigURl="getConfig/{deviceID}/{token}/{devicetype}";
    public static final String AddPacketData="session";
    public static final String evenType="et";
    public static final String evenTime="t";
    public static final String sessionEventStartCode="11";
    public static final String sessionEventEndCode="12";
    public static final String APPCRASH = "app_crash";
    public static final String DEV_BUG = "Developer Bug";
    public static final String ADD_DEVICE = "device";
    public static final String SESSION_ID = "session_id";
    public static final String UPLOAD_ZIPFILE = "uploadZip";




    public final static class PINCH {
        public final static int PINCH_IN = 0;
        public final static int PINCH_OUT = 1;
    }

    public final static class VIDEO_QUALITY {
        public final static int DEFAULT_FPS = 5;
        public final static int DEFAULT_CRF = 25;
    }

    public static class Build {
        public enum TYPE {
            DEV,
            PROD;
        }
    }

    public static class EVENT {
        public enum TYPE {
            SESSION_START(54),
            SESSION_END(55),
            MERGE_SESSION(66),
            SESSION_INTERNAL_PROPERTIES(65),
            VIEW_START(56),
            VIEW_END(57),
            NATIVE_PAGE_VIEW(18),
            CLICK(32),
            NATIVE_DEVICE(58),
            DEVICE_ORIENTATION(60),
            SHAKE(62),
            PINCH(35),
            SWIPE(34),
            CUSTOM_EVENT(30),
            SESSION_PROPERTY(52),
            USER_PROPERTY(51),
            IDENTITY(53),
            CRASH(27);

            private final int value;

            private TYPE(int value) {
                this.value = value;
            }

            public int getValue() {
                return value;
            }
        }

        public enum STATUS {
            READY_TO_UPLOAD,
            UPLOADING,
            TEMP,
            UPLOADED,
            CREATING,
            CREATED
        }
    }

    public static class SWIPE {
        public enum Direction {
            UP(8),
            DOWN(16),
            LEFT(2),
            RIGHT(4);

            private int value;

            public int getValue() {
                return value;
            }

            private Direction(int value) {
                this.value = value;
            }

            /**
             * Returns a direction given an angle.
             * Directions are defined as follows:
             * <p>
             * Up: [45, 135]
             * Right: [0,45] and [315, 360]
             * Down: [225, 315]
             * Left: [135, 225]
             *
             * @param angle an angle from 0 to 360 - e
             * @return the direction of an angle
             */
            public static Direction get(double angle) {
                if(inRange(angle, 45, 135)) {
                    return Direction.UP;
                } else if(inRange(angle, 0, 45) || inRange(angle, 315, 360)) {
                    return Direction.RIGHT;
                } else if(inRange(angle, 225, 315)) {
                    return Direction.DOWN;
                } else {
                    return Direction.LEFT;
                }

            }

            /**
             * @param angle an angle
             * @param init  the initial bound
             * @param end   the final bound
             * @return returns true if the given angle is in the interval [init, end).
             */
            private static boolean inRange(double angle, float init, float end) {
                return (angle >= init) && (angle < end);
            }
        }
    }


    public static class RequestBuilder {

        //Login request body
        public static RequestBody LoginBody(String username, String password, String token) {
            return new FormBody.Builder()
                    .add("action", "login")
                    .add("format", "json")
                    .add("username", username)
                    .add("password", password)
                    .add("logintoken", token)
                    .build();
        }

        public static HttpUrl buildURL(String url) {
            return new HttpUrl.Builder()
                    .scheme("http") //http // http://loganalyticssession.shrigowri.com:5551/
                    .host("loganalyticssession.shrigowri.com")
                    .port(5551)
                    .addPathSegment("getConfig")//adds "/pathSegment" at the end of hostname
                    .addPathSegment("43214321432423")//adds "/pathSegment" at the end of hostname
                    .addPathSegment("d8816c20-8e84-4b0d-96f9-1ef638c3c141")//adds "/pathSegment" at the end of hostname
                    .addPathSegment("android")//adds "/pathSegment" at the end of hostname
                    .build();
            /**
             * The return URL:
             *  https://www.somehostname.com/pathSegment?param1=value1&encodedName=encodedValue
             */
        }

    }
    public static class ORIENTATION {
        public enum Direction {
            LEFT(270),
            RIGHT(90),
            PORTRAIT(0),
            DOWN(180);

            private int value;

            public int getValue() {
                return value;
            }

            private Direction(int value) {
                this.value = value;
            }

            public static Direction fromAngle(int angle) {
                for(Direction direction : Direction.values()) {
                    if(direction.getValue() == angle) {
                        return direction;
                    }
                }
                return null;
            }

        }
    }

}
