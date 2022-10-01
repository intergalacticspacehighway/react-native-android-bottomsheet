import React from "react"
import { AndroidBottomsheetView } from 'react-native-android-bottomsheet';
import { View} from 'react-native';

type BottomSheetProps = {
    visible: boolean;
    onDismiss: () => void;
    peekHeight: number
    maxHeight: number;
    children: React.ReactNode;
    "aria-label": string
}

export const BottomSheet = (props: BottomSheetProps) => {
    if (!props.visible) {
        return null;
    }

    return <AndroidBottomsheetView {...props} style={{position: "absolute"}} >
        <View  style={{flex: 1}} collapsable={false}>
            {props.children}
        </View>
    </AndroidBottomsheetView>
}