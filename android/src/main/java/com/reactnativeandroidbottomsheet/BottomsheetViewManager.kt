package com.reactnativeandroidbottomsheet
import androidx.annotation.Nullable
import com.facebook.react.common.MapBuilder
import com.facebook.react.uimanager.ThemedReactContext
import com.facebook.react.uimanager.UIManagerHelper
import com.facebook.react.uimanager.ViewGroupManager
import com.facebook.react.uimanager.annotations.ReactProp


class BottomsheetViewManager : ViewGroupManager<BottomSheetView>() {
  override fun getName() = "AndroidBottomsheetView"
  override fun createViewInstance(reactContext: ThemedReactContext): BottomSheetView {
    return BottomSheetView(reactContext)
  }

  @ReactProp(name = "visible")
  fun setVisible(view: BottomSheetView, visible: Boolean) {
    if (visible) {
      view.showBottomSheet()
    }

    if (!visible && view.isVisible() == true) {
      view.hideBottomSheet()
    }
  }

  @ReactProp(name = "peekHeight")
  fun setPeekHeight(view: BottomSheetView, peekHeight: Int) {
      view.setBottomSheetPeekHeight(peekHeight)
  }

  @ReactProp(name = "cancelable")
  fun setCancelable(view: BottomSheetView, cancelable: Boolean) {
    view.setCancelable(cancelable)
  }

  @ReactProp(name = "aria-label")
  fun setAriaLabel(view: BottomSheetView, title: String) {
    view.setAriaLabel(title)
  }

  @ReactProp(name = "maxHeight")
  fun setMaxHeight(view: BottomSheetView, maxHeight: Int) {
    view.setBottomSheetMaxHeight(maxHeight)
  }

  @ReactProp(name = "backdropDimAmount")
  fun setBackdropDimAmount(view: BottomSheetView, dimAmount: Float) {
    view.setBackdropDimAmount(dimAmount)
  }

  override fun addEventEmitters(reactContext: ThemedReactContext, view: BottomSheetView) {
    val dispatcher = UIManagerHelper.getEventDispatcher(reactContext, view.id);
    if (dispatcher != null) {
      view.setEventDispatcher(dispatcher)
      view.setOnDismissListener(dispatcher)
    };
  }


  override fun getExportedCustomDirectEventTypeConstants(): Map<String, Any>? {
    @Nullable val baseEventTypeConstants = super.getExportedCustomDirectEventTypeConstants()
    val eventTypeConstants =
      baseEventTypeConstants ?: HashMap()
    eventTypeConstants.putAll(
      mapOf(DismissBottomSheetEvent.getEventName() to MapBuilder.of("registrationName", "onDismiss"))
    )
    return eventTypeConstants
  }
}
