// import { url } from 'inspector';
import React, { Component } from 'react';
import VideoPlayer from 'react-video-js-player';
import Videos from 'url'


     const VideoApp = ({vid}) => {
    {
        return (
            <div>
           <VideoPlayer
                    controls={true}
                    src= {vid}
                    poster={'https://images.pexels.com/photos/2253275/pexels-photo-2253275.jpeg'}
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

