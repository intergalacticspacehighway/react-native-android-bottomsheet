import * as React from 'react';

import {  View , ScrollView,Text, Button,TextInput } from 'react-native';
import { BottomSheet } from './bottom-sheet';


export default function App() {
  const [visible1, setVisible1] = React.useState(false);
  const [visible2, setVisible2] = React.useState(false);
  const [visible3, setVisible3] = React.useState(false);
  const [visible4, setVisible4] = React.useState(false);

  return (
    <View style={{flex: 1}}>
      <View style={{marginTop: 50}}>
        <Button title="bottomsheet" onPress={() => setVisible1(!visible1)} />
      </View>

      <View style={{marginTop: 10}}>
        <Button title="bottomsheet with scrollview" onPress={() => setVisible2(!visible2)} />
      </View>

      <View style={{marginTop: 10}}>
        <Button title="bottomsheet with textinput" onPress={() => setVisible3(!visible3)} />
      </View>


      <View style={{marginTop: 10}}>
        <Button title="multiple bottomsheet" onPress={() => setVisible4(!visible4)} />
      </View>

      <BottomSheet peekHeight={400} maxHeight={1000} aria-label="Edit your profile"  visible={visible1} onDismiss={() => {
        setVisible1(false);
      }}>
        <View style={{flex: 1}}>
          <Text>Hello from bottomsheet</Text>
          </View>
      </BottomSheet>

      
      <BottomSheet visible={visible2} onDismiss={() => {
        setVisible2(false);
      }}>
        <ScrollView nestedScrollEnabled>
          <View style={{width: 100, height: 10000, backgroundColor:"black"}} />
        </ScrollView>
      </BottomSheet>

       
      <BottomSheet aria-label="Edit your profile"  visible={visible3} onDismiss={() => {
        setVisible3(false);
      }}>
        <TextInput placeholder="Enter name"  style={{width: "100%", height: 80, marginTop: 400, borderWidth: 2}} />
          <View style={{width: "100%", height: 10000, backgroundColor:"black"}} />
      </BottomSheet>


      <BottomSheet visible={visible4} onDismiss={() => {
        setVisible4(false);
      }}>
        <ScrollView nestedScrollEnabled>
        <View style={{marginTop: 10}}>
        <Button title="bottomsheet" onPress={() => setVisible1(!visible1)} />
      </View>
          <View style={{width: 100, height: 10000, backgroundColor:"black"}} />
        </ScrollView>
      </BottomSheet>

    </View>
  );
}
