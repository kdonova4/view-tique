import { useEffect, useState } from "react";
import { Link, useParams } from "react-router-dom";
import ReviewForm from "./ReviewForm";
import { Button } from "react-bootstrap";
import { useAuth } from "./context/AuthContext";
import { jwtDecode } from "jwt-decode";

function ReviewList({ refreshData }){

    const [reviews, setReviews] = useState([]);
    const [showModal, setShowModal] = useState(false);
    const [currentReviewId, setCurrentReviewId] = useState(null);
    const url = 'http://localhost:8080/v1/api/reviews/game'
    const { token, role, appUserId } = useAuth();

    const handleOpenModal = (reviewId = null) => {
        setCurrentReviewId(reviewId);
        setShowModal(true);
    }

    const decodedToken = token ? jwtDecode(token) : null;
    const handleCloseModal = () => {
        setShowModal(false);
        setCurrentReviewId(null);
    }

    const { gameId } = useParams();

    useEffect(() => {
        fetch(`${url}/${gameId}`)
        .then(response =>{
            if(response.status === 200) {
                return response.json();
            }else{
                return Promise.reject(`Unexpected Status Code ${response.status}`)
            }
        })
        .then(data => setReviews(data))
        .catch(console.log);
    }, [gameId])


    const fetchReviews = () => {
        fetch(`${url}/${gameId}`)
        .then(response =>{
            if(response.status === 200) {
                return response.json();
            }else{
                return Promise.reject(`Unexpected Status Code ${response.status}`)
            }
        })
        .then(data => setReviews(data))
        .catch(console.log);
    }

    return(<>
        <section>
                <h2 className='mb-4'>Reviews</h2>
                <Button variant="primary" onClick={() => {
                    if(!token) {
                        alert('Please Register or Log In to add a review')
                    }else if(role === 'ROLE_ADMIN') {
                        alert('Admins CANNOT Review Games')
                    } else{
                        handleOpenModal();
                    }
                }}>
                    Add Review
                </Button>
                
                        <table className='table table-striped'>
                            <thead className='thead-dark'>
                                <tr>
                                    <th>Score</th>
                                    <th>Timestamp</th>
                                    <th>Review Body</th>
                                    <th>Likes</th>
                                    <th>Dislikes</th>
                                    <th>&nbsp;</th>
                                </tr>
                            </thead>
                            <tbody>
                                {reviews.map(review => (
                                    <tr key={review.reviewId}>
                                        <td>{review.score}</td>
                                        <td>{new Date(review.timestamp).toLocaleString(undefined, { 
                                            year: 'numeric', 
                                            month: 'long', 
                                            day: 'numeric', 
                                            hour: 'numeric', 
                                            minute: '2-digit'
                                            })}
                                        </td>
                                        <td>{review.reviewBody}</td>
                                        <td>{review.likes}</td>
                                        <td>{review.dislikes}</td>
                                        <td>
                                        {decodedToken && review.userId === decodedToken.appUserId && (
                                            <Button onClick={() => handleOpenModal(review.reviewId)}>Edit</Button>
                                        )}
                                        </td>
                                    </tr>
                                ))}
                            </tbody>
                        </table>
                                {showModal && (
                                    <ReviewForm  fetchReviews={fetchReviews} refreshData={refreshData} reviewId={currentReviewId} onClose={handleCloseModal}/>
                                )}

                    </section>
    </>)
}

export default ReviewList;