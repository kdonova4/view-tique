import { useEffect, useState } from "react";
import { FaThumbsUp, FaThumbsDown, FaRegThumbsDown, FaRegThumbsUp } from "react-icons/fa";


function Reactions({ likes, dislikes, reviewId, userId, token , fetchReviews }) {

    const [reaction, setReaction] = useState("");
    const [errors, setErrors] = useState([])
    const url = 'http://3.13.229.3:8080/api/v1/reactions'

    const handleCreate = (reviewId, userId, reactionType) => {
        const init = {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json',
                'Authorization': `Bearer ${token}`
            },
            body: JSON.stringify({
                reviewId: reviewId,
                userId: userId,
                reactionType: reactionType
            })
        }
        fetch(url, init)
        .then(response => {
            if(response.status === 201) {
                
                setReaction(reactionType)
                fetchReviews();
                return response.json()
            } else if(response.status === 400) {
                return response.json()
            }else {
                return Promise.reject(`Unexpected Status Code ${response.status}`)
            }
        })
        .then(data => {
            if(!data.reactionId) {
                setErrors(data);
            }
        })
        .catch(console.log)
    }
    
    const handleDelete = (reactionId) => {
        const headers = {
            'Authorization': `Bearer ${token}`,
            'Content-Type': 'application/json'
        }
        const init = {
            method: 'DELETE',
            headers: headers
        }

        return fetch(`${url}/${reactionId}`, init)
        .then(response => {
            if(response.status === 204) {
                setReaction("")
                fetchReviews();
            } else {
                return Promise.reject(`Unexpected Status Code ${response.status}`)
            }
        })
        .catch(console.log)
    }

    useEffect(() => {
        fetch(`${url}/${reviewId}/${userId}`)
            .then(response => {
                if (response.status === 200) {
                    return response.json();
                } else {
                    return Promise.reject(`Unexpected Status Code ${response.status}`);
                }
            })
            .then(data => {
                setReaction(data.reactionType); // Set reaction type from server
            })
            .catch(console.log);
    }, []);

    const findReaction = (reviewId, userId, reactionType) => {

        if(!token) {
            alert("Please Login to React to Reviews")
            return;
        }
        fetch(`${url}/${reviewId}/${userId}`)
        .then(response => {
            if(response.status === 200) {
                return response.json()
            } else if(response.status === 404){
                handleCreate(reviewId, userId, reactionType);
                return null;
            }else {
                return Promise.reject(`Unexpected Status Code ${response.status}`)
            }
        })
        .then(data => {
            
            if(data) {
                if(data.reactionType === reactionType) {
                    handleDelete(data.reviewReactionId);
                } else {
                    handleDelete(data.reviewReactionId).then(() => {
                        handleCreate(reviewId, userId, reactionType);
                    })
                    
                }
                    
            }
        })
        .catch(console.log)
    }

    return(
        <>
        <div className="reaction-block">
        <div className="mr-4">
            <span className="reaction" onClick={() => findReaction(reviewId, userId, "LIKE")}>
            {reaction === "LIKE" ? <FaThumbsUp /> : <FaRegThumbsUp />}
            </span> {likes}
        </div>
        <div className="mr-4">
            <span className="reaction" onClick={() => findReaction(reviewId, userId, "DISLIKE")}>
                
            {reaction === "DISLIKE" ? <FaThumbsDown /> : <FaRegThumbsDown />}
                
            </span> {dislikes}
        </div>
        </div>
        
        
        </>
    )
}

export default Reactions;