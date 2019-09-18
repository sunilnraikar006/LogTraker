package com.sgs.mylibrary.screenshot;

import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.content.ContextWrapper;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.os.Build;
import android.os.Looper;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager.LayoutParams;


import com.sgs.mylibrary.logtracker.LogManager;

import java.lang.ref.WeakReference;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.CountDownLatch;



import static android.graphics.Bitmap.Config.ARGB_8888;
import static android.view.WindowManager.LayoutParams.FLAG_DIM_BEHIND;

/**
 * Utility class to take screenshots of activity screen
 */
public final class ScreenShotHelper {
    //region Constants

    private static final String TAG = "ScreenShotHelper";
    public static Object globalWindowManager;

    //endregion

    //region Public API

    /**
     * Takes screenshot of provided activity and puts it into bitmap.
     *
     * @param activity Activity of which the screenshot will be taken.
     * @return Bitmap of what is displayed in activity.
     * @throws UnableToTakeScreenshotException When there is unexpected error during taking screenshot
     */
    public static Bitmap takeScreenshotBitmap(Activity activity, int width, int height) throws Exception {
        //activity = activity== null ? : activity;
        if (activity == null) {
            throw new
                    IllegalArgumentException("Activity cannot be null.");
        }
        try {
            return takeBitmapUnchecked(activity, width, height);
        } catch (Exception e) {
            String message = "Unable to take screenshot to bitmap of activity "
                    + activity.getClass().getName();

            // Log.e(TAG, message, e);
            throw new UnableToTakeScreenshotException(message, e);
        }
    }



    /**
     * method to take bitmap
     * @param activity
     * @param width
     * @param height
     * @return Bitmap of what is displayed in activity
     * @throws 'InterruptedException When there is unexpected error during taking screenshot
     */
    private static Bitmap takeBitmapUnchecked(Activity activity, int width, int height) throws InterruptedException {

        Window window = activity.getWindow();

        final ViewGroup viewGroup = (ViewGroup) ((ViewGroup) window.getDecorView()
                .findViewById(android.R.id.content)).getChildAt(0);

        List<ViewRootData> roots = getRootViews(activity);
        final List<ViewRootData> viewRoots = new WeakReference<>(roots).get();

        if (viewRoots.isEmpty()) {
          /*  if (Constant.SDK_MODE.contentEquals(Constant.Build.TYPE.DEV.name())) {
                throw new UnableToTakeScreenshotException("Unable to capture any view data in " + activity);
            }*/
        }
        int maxWidth = Integer.MIN_VALUE;
        int maxHeight = Integer.MIN_VALUE;
        for (ViewRootData viewRoot : viewRoots) {
            if (viewRoot._winFrame.right > maxWidth) {
                maxWidth = viewRoot._winFrame.right;
            }
            if (viewRoot._winFrame.bottom > maxHeight) {
                maxHeight = viewRoot._winFrame.bottom;
            }
        }
        final Bitmap bitmap = Bitmap.createBitmap(width, height, ARGB_8888);
        //drawRootsToBitmap(viewRoots, bitmap);
        // We need to do it in main thread

        try {
            if ((int) window.getDecorView().getTag() == LogManager.MASK_CODE) {
                //  Paint hidePaint = new Paint();
                // hidePaint.setColor(Color.rgb(0, 0, 0));
                Canvas dimCanvas = new Canvas(bitmap);
                int alpha = (int) (255 * 0.3);
                dimCanvas.drawARGB(alpha, 0, 0, 0);
                return bitmap;
            }
        } catch (Exception e) {
            //e.printStackTrace();
        }

        if (Looper.myLooper() == Looper.getMainLooper()) {
            drawRootsToBitmap(viewRoots, bitmap);
        } else {
            final CountDownLatch latch = new CountDownLatch(1);
            activity.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    try {
                        drawRootsToBitmap(viewRoots, bitmap);
                    } finally {
                        latch.countDown();
                    }
                }
            });

            latch.await();
        }

        return bitmap;
    }

    /**
     * method to draw the roots to bitmap with @view roots and bitmap
     * @param viewRoots
     * @param bitmap
     */
    private static void drawRootsToBitmap(List<ViewRootData> viewRoots, Bitmap bitmap) {
        for (ViewRootData rootData : viewRoots) {
            try {
                drawRootToBitmap(rootData, bitmap);
            } catch (NullPointerException e) {
            }
            continue;
        }
    }

    /**
     * method to draw the roots to the bitmap with instance of root and bitmap
     * @param config
     * @param bitmap
     * @throws "NullPointerException"
     */
    private static void drawRootToBitmap(ViewRootData config, Bitmap bitmap) throws NullPointerException {
        // now only dim supported
        if (config._view == null)
            return;
        if ((config._layoutParams.flags & FLAG_DIM_BEHIND) == FLAG_DIM_BEHIND) {
            Canvas dimCanvas = new Canvas(bitmap);
            int alpha = (int) (255 * config._layoutParams.dimAmount);
            dimCanvas.drawARGB(alpha, 0, 0, 0);
        }
        Canvas canvas = new Canvas(bitmap);
        canvas.translate(config._winFrame.left, config._winFrame.top);
        hideViews = new ArrayList<>();
        parseView(config._view);
        config._view.draw(canvas);

        Paint hidePaint = new Paint();
        hidePaint.setColor(Color.rgb(0, 0, 0));

        for (View view : hideViews) {
            Rect hideRect = new Rect();
            view.getGlobalVisibleRect(hideRect);
            canvas.drawRect(hideRect, hidePaint);
        }
    }

    private static ArrayList<View> hideViews = new ArrayList<>();

    /**
     * method to parse the view
     * @param view
     */
    private static void parseView(View view) {
        if (view instanceof ViewGroup) {
            ViewGroup parent = (ViewGroup) view;
            for (int i = 0; i < parent.getChildCount(); i++) {
                parseView(parent.getChildAt(i));
            }
        }

        try {
            int tag = (int) view.getTag();
            if (tag == LogManager.MASK_CODE) {
                 hideViews.add(view);
            }
        } catch (Exception e) {
         //   e.printStackTrace();
        }

    }

    /**
     * method returns the object
     * @param activity
     * @return
     */
    public static Object getGlobalWindowManager(Activity activity) {
        if (globalWindowManager != null)
            return globalWindowManager;
   /*     if(Build.VERSION.SDK_INT <= Build.VERSION_CODES.JELLY_BEAN) {
            globalWindowManager = getFieldValue("mWindowManager", activity.getWindowManager());
        } else */
        {
            globalWindowManager = getFieldValue("mGlobal", activity.getWindowManager());
        }
        return globalWindowManager;
    }


    /**
     * method returns thr list of root views
     * @param activity
     * @return root views for the particular activity
     */
    private static List<ViewRootData> getRootViews(Activity activity) {
        //Object globalWindowManager;
        globalWindowManager = getGlobalWindowManager(activity);
        Object rootObjects;
        Object paramsObject;
        rootObjects = new WeakReference(getFieldValue("mRoots", globalWindowManager)).get();
        paramsObject = new WeakReference(getFieldValue("mParams", globalWindowManager)).get();

        Object[] roots;
        LayoutParams[] params;

        //  There was a change to ArrayList implementation in 4.4
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            roots = ((List) rootObjects).toArray();
            List<LayoutParams> paramsList = (List<LayoutParams>) paramsObject;
            params = paramsList.toArray(new LayoutParams[paramsList.size()]);
        } else {
            roots = (Object[]) rootObjects;
            params = (LayoutParams[]) paramsObject;
        }
        List<ViewRootData> rootViews = viewRootData(roots, params);
        if (rootViews.isEmpty()) {
            return Collections.emptyList();
        }
        offsetRootsTopLeft(rootViews);
        ensureDialogsAreAfterItsParentActivities(rootViews);
        return rootViews;

    }

    /**
     * @param roots
     * @param params
     * @return root view for the corresponding activity
     */
    private static List<ViewRootData> viewRootData(Object[] roots, LayoutParams[] params) {
        List<ViewRootData> rootViews = new ArrayList<>();
        for (int i = 0; i < roots.length; i++) {
            Object root = roots[i];
            View view = new WeakReference<View>((View) getFieldValue("mView", root)).get();
            // fixes https://github.com/jraska/Falcon/issues/10
            if (view == null) {
                Log.e(TAG, "null View stored as root in Global window manager, skipping");
                continue;
            }
            if (!view.isShown()) {
                continue;
            }
            Object attachInfo = getFieldValue("mAttachInfo", root);
            int top = (int) getFieldValue("mWindowTop", attachInfo);
            int left = (int) getFieldValue("mWindowLeft", attachInfo);
            Rect winFrame = (Rect) getFieldValue("mWinFrame", root);
            Rect area = new Rect(left, top, left + winFrame.width(), top + winFrame.height());
            rootViews.add(new ViewRootData(view, area, params[i]));
        }

        return rootViews;
    }

    /**
     * method used to get the offset values for the rootviews
     * @param rootViews
     */
    private static void offsetRootsTopLeft(List<ViewRootData> rootViews) {
        int minTop = Integer.MAX_VALUE;
        int minLeft = Integer.MAX_VALUE;
        for (ViewRootData rootView : rootViews) {
            if (rootView._winFrame.top < minTop) {
                minTop = rootView._winFrame.top;
            }

            if (rootView._winFrame.left < minLeft) {
                minLeft = rootView._winFrame.left;
            }
        }

        for (ViewRootData rootView : rootViews) {
            rootView._winFrame.offset(-minLeft, -minTop);
        }
    }



    /**
     * This fixes issue #11. It is not perfect solution and maybe there is another case
     * of different type of view, but it works for most common case of dialogs.
     * @param viewRoots
     */
    private static void ensureDialogsAreAfterItsParentActivities(List<ViewRootData> viewRoots) {
        if (viewRoots.size() <= 1) {
            return;
        }

        for (int dialogIndex = 0; dialogIndex < viewRoots.size() - 1; dialogIndex++) {
            ViewRootData viewRoot = viewRoots.get(dialogIndex);
            if (!viewRoot.isDialogType()) {
                continue;
            }

            Activity dialogOwnerActivity = ownerActivity(viewRoot.context());
            if (dialogOwnerActivity == null) {
                // make sure we will never compare null == null
                return;
            }

            for (int parentIndex = dialogIndex + 1; parentIndex < viewRoots.size(); parentIndex++) {
                ViewRootData possibleParent = viewRoots.get(parentIndex);
                if (possibleParent.isActivityType()
                        && ownerActivity(possibleParent.context()) == dialogOwnerActivity) {
                    viewRoots.remove(possibleParent);
                    viewRoots.add(dialogIndex, possibleParent);

                    break;
                }
            }
        }
    }

    /**
     * @param context
     * @return activity of that particular screen with help of context
     */
    private static Activity ownerActivity(Context context) {
        Context currentContext = context;

        while (currentContext != null) {
            if (currentContext instanceof Activity) {
                return (Activity) currentContext;
            }

            if (currentContext instanceof ContextWrapper && !(currentContext instanceof Application)) {
                currentContext = ((ContextWrapper) currentContext).getBaseContext();
            } else {
                break;
            }
        }

        return null;
    }

    /**
     *
     * @param fieldName
     * @param target
     * @return Field object value
     */
    private static Object getFieldValue(String fieldName, Object target) {
        try {
            return getFieldValueUnchecked(fieldName, target);
        } catch (Exception e) {
            throw new UnableToTakeScreenshotException(e);
        }
    }

    /**
     *
     * @param fieldName
     * @param target
     * @return Object of FieldClass
     * @throws "NoSuchFieldException
     * @throws "IllegalAccessException
     */
    private static Object getFieldValueUnchecked(String fieldName, Object target)
            throws NoSuchFieldException, IllegalAccessException {
        Field field = findField(fieldName, target.getClass());

        field.setAccessible(true);
        return field.get(target);
    }

    /**
     *
     * @param name
     * @param clazz
     * @return Field Object
     * @throws 'NoSuchFieldException
     */
    private static Field findField(String name, Class clazz) throws NoSuchFieldException {
        Class currentClass = clazz;
        while (currentClass != Object.class) {
            for (Field field : currentClass.getDeclaredFields()) {

                if (name.equals(field.getName())) {
                    return field;
                }
            }

            currentClass = currentClass.getSuperclass();
        }

        throw new NoSuchFieldException("Field " + name + " not found for class " + clazz);
    }

    private ScreenShotHelper() {
    }

    /**
     * Custom exception thrown if there is some exception thrown during
     * screenshot capturing to enable better client code exception handling.
     */
    public static class UnableToTakeScreenshotException extends RuntimeException {
        private UnableToTakeScreenshotException(String detailMessage) {
            super(detailMessage);
        }

        private UnableToTakeScreenshotException(String detailMessage, Exception exception) {
            super(detailMessage, extractException(exception));
        }

        private UnableToTakeScreenshotException(Exception ex) {
            super(extractException(ex));
        }

        /**
         * Method to avoid multiple wrapping. If there is already our exception,
         * just wrap the cause again
         */
        private static Throwable extractException(Exception ex) {
            if (ex instanceof UnableToTakeScreenshotException) {
                return ex.getCause();
            }

            return ex;
        }
    }

    /**
     * ViewRootData Class
     */
    private static class ViewRootData {
        private final View _view;
        private final Rect _winFrame;
        private final LayoutParams _layoutParams;

        ViewRootData(View view, Rect winFrame, LayoutParams layoutParams) {
            _view = view;
            //_view.invalidate();
            _winFrame = winFrame;
            _layoutParams = layoutParams;

        }

        boolean isDialogType() {
            return _layoutParams.type == LayoutParams.TYPE_APPLICATION;
        }

        boolean isActivityType() {
            return _layoutParams.type == LayoutParams.TYPE_BASE_APPLICATION;
        }

        Context context() {
            return _view.getContext();
        }
    }
}
