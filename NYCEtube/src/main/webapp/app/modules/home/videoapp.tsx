// import { url } from 'inspector';
import React, { Component } from 'react';
import { Router } from 'react-router-dom';
import VideoPlayer from 'react-video-js-player';
import Videos from 'url'

    
     const VideoApp = ({vid}) => {
    {
        
        return (
            
            <div>
            
           <VideoPlayer
                    
                    controls={true}
                    src= {vid.url}
                    poster={vid.poster}
                    width="720"
                    height="420"
                    onLoadedData= "auto"
                    type="video/mp4"
                />
                
                </div>
                
        );
    }
}





export default VideoApp;

