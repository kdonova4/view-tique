import { useEffect, useState } from "react";
import { Blurhash } from "react-blurhash";

function ImageComponent({ src }) {

    const [imageLoaded, setImageLoaded] = useState(false);
    const [width, setWidth] = useState();
    const [height, setHeight] = useState();
    useEffect(() => {
        const image = new Image()
        image.onload = () => {
            setImageLoaded(true);
            setWidth(image.naturalWidth)
            setHeight(image.naturalHeight)
        }
        image.src = src;

    }, [src])


    return (
        <>
            {!imageLoaded && (
                <Blurhash
                    hash="U6PZfSjE.AyE_3t7t7R**0o#DgR4_3R*D%xs"
                    width={width}
                    height={height}
                    resolutionX={32}
                    resolutionY={32}
                    punch={1}
                />
            )}
            {imageLoaded && (
                <img 
                    className="scale-down"
                    onLoad={() => setImageLoaded(true)}
                    src={src}
                    
                    alt=""
                />
            )}

        </>
    )
}

export default ImageComponent;