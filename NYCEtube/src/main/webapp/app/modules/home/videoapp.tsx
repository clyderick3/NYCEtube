// import { url } from 'inspector';
import React, { Component } from 'react';
import VideoPlayer from 'react-video-js-player';
import Videos from 'url'


     const VideoApp = ({vid}) => {
    {
        return (
            <div>
           <VideoPlayer
                    preload= "metadata"
                    controls={true}
                    src= {vid.url}
                    poster={vid.poster}
                    width="720"
                    height="420"
                   // onReady={this.onPlayerReady.bind(this)}
                    type="video/mp4"
                />
                </div>
        );
    }
}





export default VideoApp;

