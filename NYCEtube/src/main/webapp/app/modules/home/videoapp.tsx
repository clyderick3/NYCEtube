// import { url } from 'inspector';
import React, { Component, useState, useEffect } from 'react';
import { Router } from 'react-router-dom';
import VideoPlayer from 'react-video-js-player';
import Videos from 'url'

type Props = {
    children: React.ReactElement;
    waitBeforeShow?: number;
  };
  
  const Delayed = ({ children, waitBeforeShow = 500 }: Props) => {
    const [isShown, setIsShown] = useState(false);
  
    useEffect(() => {
    //   console.log(waitBeforeShow);
      setTimeout(() => {
        setIsShown(true);
      }, waitBeforeShow);
    }, [waitBeforeShow]);
  
    return isShown ? children : null;
  };

     const VideoApp = ({vid}) => {
    {
        
        return (
            <Delayed>
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
            </Delayed>
        );
    }
}





// export default Delayed;
export default VideoApp;




