package com.forbitbd.myplayer.rtmp;

public class RTMPPlayerPresenter implements RTMPPlayerContract.Presenter {


    private RTMPPlayerContract.View mView;

    public RTMPPlayerPresenter(RTMPPlayerContract.View mView) {
        this.mView = mView;
    }
}
