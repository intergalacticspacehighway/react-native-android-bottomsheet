import * as React from 'react';

import {  View , ScrollView, Button,TextInput } from 'react-native';
import { BottomSheet } from './bottom-sheet';


export default function App() {
  const [visible1, setVisible1] = React.useState(false);
  const [visible2, setVisible2] = React.useState(false);
  const [visible3, setVisible3] = React.useState(false);

  return (
    <View style={{flex: 1}}>
      <View style={{marginTop: 50}}>
        <Button title="open bottomsheet" onPress={() => setVisible1(!visible1)} />
      </View>

      <View style={{marginTop: 10}}>
        <Button title="open bottomsheet with scrollview" onPress={() => setVisible2(!visible2)} />
      </View>

      <View style={{marginTop: 10}}>
        <Button title="open bottomsheet with textinput" onPress={() => setVisible3(!visible2)} />
      </View>

      <BottomSheet visible={visible1} onDismiss={() => {
        setVisible1(false);
      }}>
        <View style={{width: 100, height: 1000, backgroundColor:"black"}} />
      </BottomSheet>

      
      <BottomSheet visible={visible2} onDismiss={() => {
        setVisible2(false);
      }}>
        <ScrollView nestedScrollEnabled>
          <View style={{width: 100, height: 10000, backgroundColor:"black"}} />
        </ScrollView>
      </BottomSheet>

       
      <BottomSheet visible={visible3} onDismiss={() => {
        setVisible3(false);
      }}>
        <TextInput placeholder="hello world"  style={{width: 100, height: 100, marginTop: 400}} />
          <View style={{width: "100%", height: 10000, backgroundColor:"black"}} />
      </BottomSheet>

    </View>
  );
}
