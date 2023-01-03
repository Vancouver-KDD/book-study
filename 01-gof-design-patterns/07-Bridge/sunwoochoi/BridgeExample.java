/*
  Test output: 

  Call button is clicked
  Calling with Sunwoo
  get public IP address and Port number
  negotiate with peer 1
  send video stream to peer 1
  End button is clicked

  Call button is clicked
  Calling with Sunwoo
  send request to server to connect 1
  send video stream to server
  End button is clicked
  Send request to server to disconnect 1
  set volume from 5 to 4
  mute volume
  set volume from 4 to 0
 */
class BridgeExample {
  /* Client */
  public static void main(String[] args) {
    Peer peer = new Peer(1, "Sunwoo");

    VideoCallHandler p2pHandler = new P2PVideoCallHandler();

    VideoCallUI ui = new VideoCallUI(p2pHandler);
    ui.clickCallButton(peer);
    ui.clickEndButton();

    VideoCallHandler relayHandler = new RelayVideoCallHandler();

    VideoCallUIwVolumeController ui2 = new VideoCallUIwVolumeController(relayHandler);
    ui2.clickCallButton(peer);
    ui2.clickEndButton();
    ui2.setVolume(4);
    ui2.muteVolume();
  }
}
