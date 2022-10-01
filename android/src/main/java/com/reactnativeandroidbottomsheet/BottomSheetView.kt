package com.reactnativeandroidbottomsheet

import android.content.Context
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import android.view.accessibility.AccessibilityEvent
import android.widget.FrameLayout
import androidx.annotation.Nullable
import androidx.annotation.UiThread
import com.facebook.react.bridge.*
import com.facebook.react.uimanager.*
import com.facebook.react.uimanager.events.Event
import com.facebook.react.uimanager.events.EventDispatcher
import com.facebook.react.views.view.ReactViewGroup
import com.google.android.material.bottomsheet.BottomSheetDialog


// This source is based on React Native's Modal component
// https://github.com/facebook/react-native/blob/f1645560376b734a87f0eba1fef69f6cba312cc1/ReactAndroid/src/main/java/com/facebook/react/views/modal/ReactModalHostView.java

class BottomSheetView: ViewGroup, FabricViewStateManager.HasFabricViewStateManager {
  lateinit var mHostView: BottomSheetViewGroup;
  @Nullable
  private var bottomSheetDialog: BottomSheetDialog? = null;


  constructor(context: Context) : super(context) {
      mHostView = BottomSheetViewGroup(context)
      bottomSheetDialog = BottomSheetDialog(context)
  }

  override fun addView(child: View?, index: Int) {
    UiThreadUtil.assertOnUiThread()
      mHostView.addView(child)
  }


  override fun getChildCount(): Int {
    return mHostView.childCount
  }

  override fun getChildAt(index: Int): View {
    return mHostView.getChildAt(index)
  }

  override fun removeView(child: View?) {
    UiThreadUtil.assertOnUiThread()
    mHostView.removeView(child)
  }

  override fun removeViewAt(index: Int) {
    UiThreadUtil.assertOnUiThread()
    val child = getChildAt(index)
    mHostView.removeView(child)
  }

  fun setOnDismissListener (dispatcher: EventDispatcher) {
    bottomSheetDialog?.setOnDismissListener {
      dispatcher.dispatchEvent(DismissBottomSheetEvent(UIManagerHelper.getSurfaceId(this), this.id))
    }
  }

  fun showBottomSheet() {
    val frameLayout = FrameLayout(context)
    frameLayout.addView(mHostView)
    frameLayout.fitsSystemWindows = true
    bottomSheetDialog?.setContentView(frameLayout);
    // TODO: export a prop for this
    // We make background transparent and let styles be handled with a React native view. (useful for setting rounded border radius)
//    (frameLayout.getParent() as View).setBackgroundColor(Color.TRANSPARENT)
    // bottomSheetDialog?.dismissWithAnimation = true;

  // Todo:  color/backdrop changes
  //    val cd = ColorDrawable(-0x43ff6433)
//    bottomSheetDialog?.window?.setDimAmount(0.2f)
    bottomSheetDialog?.show();
  }

  fun isVisible(): Boolean? {
    return bottomSheetDialog?.isShowing
  }

  fun hideBottomSheet() {
    UiThreadUtil.assertOnUiThread()
    if (bottomSheetDialog != null) {
      bottomSheetDialog?.hide();
      bottomSheetDialog = null
      val parent = mHostView.parent as ViewGroup
      parent.removeViewAt(0)
    }
  }


  fun setBottomSheetPeekHeight(peekHeight: Int) {
    bottomSheetDialog?.behavior?.peekHeight = peekHeight;
  }


  fun setBottomSheetMaxHeight(maxHeight: Int) {
    bottomSheetDialog?.behavior?.maxHeight = maxHeight;
  }

  fun setAriaLabel(title: String) {
    bottomSheetDialog?.setTitle(title)
  }

  override fun onLayout(changed: Boolean, l: Int, t: Int, r: Int, b: Int) {
    // Do nothing as we are laid out by UIManager
  }

  override fun getFabricViewStateManager(): FabricViewStateManager {
    return mHostView.fabricViewStateManager
  }

  override fun onDetachedFromWindow() {
    super.onDetachedFromWindow()
    hideBottomSheet()
  }


  fun setEventDispatcher(eventDispatcher: EventDispatcher) {
    mHostView.setEventDispatcher(eventDispatcher)
  }

  override fun addChildrenForAccessibility(outChildren: ArrayList<View?>?) {
    // Explicitly override this to prevent accessibility events being passed down to children
    // Those will be handled by the mHostView which lives in the dialog
  }

  override fun dispatchPopulateAccessibilityEvent(event: AccessibilityEvent?): Boolean {
    // Explicitly override this to prevent accessibility events being passed down to children
    // Those will be handled by the mHostView which lives in the dialog
    return false
  }
}

class BottomSheetViewGroup(context:Context): ReactViewGroup(context), RootView, FabricViewStateManager.HasFabricViewStateManager {
  private val mFabricViewStateManager = FabricViewStateManager()

  private val mJSTouchDispatcher = JSTouchDispatcher(this)

  private var mEventDispatcher: EventDispatcher? = null
  private var viewWidth = 0
  private var viewHeight = 0
  private var hasAdjustedSize = false


  fun setEventDispatcher(eventDispatcher: EventDispatcher) {
    mEventDispatcher = eventDispatcher
  }

  override fun onInterceptTouchEvent(event: MotionEvent?): Boolean {
    mJSTouchDispatcher.handleTouchEvent(event, mEventDispatcher)
    return super.onInterceptTouchEvent(event)
  }

  override fun onTouchEvent(event: MotionEvent?): Boolean {
    mJSTouchDispatcher.handleTouchEvent(event, mEventDispatcher)
    super.onTouchEvent(event)
    // In case when there is no children interested in handling touch event, we return true from
    // the root view in order to receive subsequent events related to that gesture
    return true
  }

  override fun onChildStartedNativeGesture(childView: View?, ev: MotionEvent?) {
    mJSTouchDispatcher.onChildStartedNativeGesture(ev, mEventDispatcher);
  }

  override fun onChildStartedNativeGesture(ev: MotionEvent?) {
    this.onChildStartedNativeGesture(null, ev);
  }

  override fun onChildEndedNativeGesture(childView: View?, ev: MotionEvent?) {
    mJSTouchDispatcher.onChildEndedNativeGesture(ev, mEventDispatcher);
  }

  override fun requestDisallowInterceptTouchEvent(disallowIntercept: Boolean) {
    // No-op - override in order to still receive events to onInterceptTouchEvent
    // even when some other view disallow that
  }

  override fun handleException(t: Throwable?) {
    getReactContext()?.handleException(RuntimeException(t));
  }

  private fun getReactContext(): ReactContext? {
    return context as ReactContext
  }

  override fun getFabricViewStateManager(): FabricViewStateManager {
    return mFabricViewStateManager;
  }


  override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
    super.onSizeChanged(w, h, oldw, oldh)
    viewHeight = h;
    viewWidth = w;
    updateFirstChildView()
  }

  private fun updateFirstChildView() {
    if (childCount > 0) {
      hasAdjustedSize = false
      val viewTag = getChildAt(0).id
      if (mFabricViewStateManager.hasStateWrapper()) {
        // This will only be called under Fabric
        updateState(viewWidth, viewHeight)
      } else {
        // TODO: T44725185 remove after full migration to Fabric
        val reactContext = getReactContext()
        reactContext?.runOnNativeModulesQueueThread(
          object : GuardedRunnable(reactContext) {
            override fun runGuarded() {
              val uiManager = getReactContext()?.getNativeModule(
                UIManagerModule::class.java
              ) ?: return
              uiManager.updateNodeSize(viewTag, viewWidth, viewHeight)
            }
          })
      }
    } else {
      hasAdjustedSize = true
    }
  }



  @UiThread
  fun updateState(width: Int, height: Int) {
    val realWidth = PixelUtil.toDIPFromPixel(width.toFloat())
    val realHeight = PixelUtil.toDIPFromPixel(height.toFloat())

    // Check incoming state values. If they're already the correct value, return early to prevent
    // infinite UpdateState/SetState loop.
    val currentState = fabricViewStateManager.stateData
    if (currentState != null) {
      val delta = 0.9.toFloat()
      val stateScreenHeight: Float =
        if (currentState.hasKey("screenHeight")) currentState.getDouble("screenHeight")
          .toFloat() else 0F
      val stateScreenWidth: Float =
        if (currentState.hasKey("screenWidth")) currentState.getDouble("screenWidth")
          .toFloat() else 0F
      if (Math.abs(stateScreenWidth - realWidth) < delta
        && Math.abs(stateScreenHeight - realHeight) < delta
      ) {
        return
      }
    }
    mFabricViewStateManager.setState {
      val map: WritableMap = WritableNativeMap()
      map.putDouble("screenWidth", realWidth.toDouble())
      map.putDouble("screenHeight", realHeight.toDouble())
      map
    }
  }

  override fun addView(child: View?, index: Int, params: LayoutParams?) {
    super.addView(child, index, params)
    if (hasAdjustedSize) {
      updateFirstChildView()
    }
  }

}


public class DismissBottomSheetEvent(surfaceId: Int, viewId: Int) : Event<DismissBottomSheetEvent>(surfaceId, viewId) {
  companion object {
    fun getEventName(): String {
      return "onDismiss"
    }

  }
  override fun getEventName(): String {
    return DismissBottomSheetEvent.getEventName()
  }

  override fun getEventData(): WritableMap? {
    return Arguments.createMap();
  }
}
