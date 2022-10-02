import * as React from 'react';

import {
  View,
  ScrollView,
  Text,
  Button,
  TextInput,
  RefreshControl,
} from 'react-native';
import { BottomSheet } from 'react-native-android-bottomsheet';

export default function App() {
  const [visible1, setVisible1] = React.useState(false);
  const [visible2, setVisible2] = React.useState(false);
  const [visible3, setVisible3] = React.useState(false);
  const [visible4, setVisible4] = React.useState(false);
  const [visible5, setVisible5] = React.useState(false);

  const [refreshing, setRefreshing] = React.useState(false);

  return (
    <View style={{ flex: 1 }}>
      <View style={{ marginTop: 50 }}>
        <Button title="bottomsheet" onPress={() => setVisible1(!visible1)} />
      </View>

      <View style={{ marginTop: 10 }}>
        <Button
          title="bottomsheet with scrollview"
          onPress={() => setVisible2(!visible2)}
        />
      </View>

      <View style={{ marginTop: 10 }}>
        <Button
          title="bottomsheet with textinput"
          onPress={() => setVisible3(!visible3)}
        />
      </View>

      <View style={{ marginTop: 10 }}>
        <Button
          title="multiple bottomsheet"
          onPress={() => setVisible4(!visible4)}
        />
      </View>

      <View style={{ marginTop: 10 }}>
        <Button
          title="scrollview with refresh control"
          onPress={() => setVisible5(!visible5)}
        />
      </View>

      <BottomSheet
        peekHeight={400}
        aria-label="Edit your profile"
        visible={visible1}
        onDismiss={() => {
          setVisible1(false);
        }}
      >
        <View
          style={{
            flex: 1,
            backgroundColor: 'white',
            padding: 20,
            borderTopLeftRadius: 30,
            borderTopRightRadius: 30,
          }}
        >
          <Text>Hello from bottomsheet</Text>
        </View>
      </BottomSheet>

      <BottomSheet
        visible={visible2}
        onDismiss={() => {
          setVisible2(false);
        }}
      >
        <ScrollView style={{ backgroundColor: 'white' }} nestedScrollEnabled>
          <Boxes />
        </ScrollView>
      </BottomSheet>

      <BottomSheet
        aria-label="Edit your profile"
        visible={visible3}
        onDismiss={() => {
          setVisible3(false);
        }}
      >
        <View
          style={{
            flex: 1,
            backgroundColor: 'white',
            padding: 20,
            borderTopLeftRadius: 30,
            borderTopRightRadius: 30,
          }}
        >
          <TextInput
            placeholder="Enter name"
            style={{
              width: '100%',
              height: 80,
              marginTop: 400,
              borderWidth: 2,
            }}
          />
        </View>
      </BottomSheet>

      <BottomSheet
        visible={visible4}
        onDismiss={() => {
          setVisible4(false);
        }}
      >
        <ScrollView nestedScrollEnabled style={{ backgroundColor: 'white' }}>
          <View style={{ marginTop: 10 }}>
            <Button
              title="bottomsheet"
              onPress={() => setVisible1(!visible1)}
            />
          </View>
          <Boxes />
        </ScrollView>
      </BottomSheet>

      <BottomSheet
        visible={visible5}
        onDismiss={() => {
          setVisible5(false);
        }}
      >
        <ScrollView
          style={{ backgroundColor: 'white' }}
          refreshControl={
            <RefreshControl
              onRefresh={() => {
                setRefreshing(true);
                setTimeout(() => {
                  setRefreshing(false);
                }, 2000);
              }}
              refreshing={refreshing}
            />
          }
        >
          <Boxes />
        </ScrollView>
      </BottomSheet>
    </View>
  );
}

const Boxes = () => {
  return (
    <>
      {Array.from({ length: 100 }, (_, i) => i).map((i) => (
        <View
          key={i}
          style={{
            width: '100%',
            height: 100,
            backgroundColor: 'grey',
            marginTop: 10,
          }}
        />
      ))}
    </>
  );
};
