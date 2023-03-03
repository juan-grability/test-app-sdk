import {Button, View, NativeModules, StyleSheet} from 'react-native';
import React from 'react';

const ECISDK = NativeModules.EciSdk;

const App = () => {
  return (
    <View style={styles.container}>
      <Button
        title="Login"
        onPress={async () => {
          const result = await ECISDK.startLoginView('eci');
          console.log(result);
        }}
      />
    </View>
  );
};

const styles = StyleSheet.create({
  container: {
    justifyContent: 'center',
    alignItems: 'center',
    height: '100%',
  },
});

export default App;
