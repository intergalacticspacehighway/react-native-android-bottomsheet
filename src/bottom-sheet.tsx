import React from 'react';
import {
  View,
  requireNativeComponent,
  UIManager,
  Platform,
} from 'react-native';

const LINKING_ERROR =
  `The package 'react-native-android-bottomsheet' doesn't seem to be linked. Make sure: \n\n` +
  Platform.select({ ios: "- You have run 'pod install'\n", default: '' }) +
  '- You rebuilt the app after installing the package\n' +
  '- You are not using Expo managed workflow\n';

type AndroidBottomsheetProps = {
  'visible': boolean;
  'onDismiss'?: () => void;
  'peekHeight'?: number;
  'maxHeight'?: number;
  'children': React.ReactNode;
  'aria-label'?: string;
  'backdropDimAmount'?: number;
  'cancelable'?: boolean;
};

const ComponentName = 'AndroidBottomsheetView';

export const AndroidBottomsheetView =
  UIManager.getViewManagerConfig(ComponentName) != null
    ? requireNativeComponent<AndroidBottomsheetProps>(ComponentName)
    : () => {
        throw new Error(LINKING_ERROR);
      };

export const BottomSheet = (props: AndroidBottomsheetProps) => {
  if (!props.visible) {
    return null;
  }

  return (
    <AndroidBottomsheetView
      {...props}
      // @ts-ignore
      style={{ position: 'absolute' }}
    >
      <View style={{ flex: 1 }} collapsable={false}>
        {props.children}
      </View>
    </AndroidBottomsheetView>
  );
};
