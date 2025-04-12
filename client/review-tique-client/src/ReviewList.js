import { useEffect, useState } from "react";
import { Link, useParams } from "react-router-dom";
import ReviewForm from "./ReviewForm";
import { Button } from "react-bootstrap";
import { useAuth } from "./context/AuthContext";
import { jwtDecode } from "jwt-decode";
import { FaThumbsUp, FaThumbsDown, FaRegThumbsDown, FaRegThumbsUp } from "react-icons/fa";
import Reactions from "./Reactions";
function ReviewList({ refreshData }){

    const [reviews, setReviews] = useState([]);
    const [showModal, setShowModal] = useState(false);
    const [currentReviewId, setCurrentReviewId] = useState(null);
    const url = 'http://3.13.229.3:8080/api/v1/reviews/game'
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

    const handleDelete = (reviewId) => {
        if(window.confirm(`Delete Review?`)) {
            const headers = {
                'Authorization': `Bearer ${token}`,
                'Content-Type': 'application/json'
            }
            const init = {
                method: 'DELETE',
                headers: headers
            }

            fetch(`http://3.13.229.3:8080/api/v1/reviews/${reviewId}`, init)
                .then(response => {
                    if(response.status === 204) {
                        const newReviews = reviews.filter(review => review.reviewId !== reviewId);
                        setReviews(newReviews);
                        refreshData();
                    } else {
                        return Promise.reject(`Unexpected Status Code ${response.status}`)
                    }
                })
                .catch(console.log)
        }
    }

    const getRatingColor = (rating) => {
      if (rating >= 8) return '#15ff0073';
      if (rating >= 6) return '#fffb0073';
      if (rating >= 5) return '#ff910073';
      return '#ff000073';
  };

    return (
        <>
          <Button
            className="mb-4"
            size="lg"
            variant="primary"
            onClick={() => {
              if (!token) {
                alert('Please Register or Log In to add a review');
              } else if (role === 'ROLE_ADMIN') {
                alert('Admins CANNOT Review Games');
              } else {
                handleOpenModal();
              }
            }}
          >
            Write Review
          </Button>
      
          {Array.isArray(reviews) && reviews.length > 0 ? (
            <section>
              
              <div className="container reviews-container">
              {reviews.map((review) => (
                <div className="review-card mb-4" key={review.reviewId}>
                    <div className="review-info mr-2">
                      <div className="review-score ml-4 mt-4">
                        <div className="circle-review mb-4" style={{ backgroundColor: getRatingColor(review.score) }}>{review.score}</div>
                      </div>
                      <div className="review-user mb-4">{review.username}</div>
                      <div className="review-date mb-4">{new Date(review.timestamp).toLocaleString(undefined, {
                          year: 'numeric',
                          month: 'long',
                          day: 'numeric',
                          hour: 'numeric',
                          minute: '2-digit',
                        })}</div>
                    </div>
                    <div className="review-body">
                      <div className="review-text mb-2 mt-4 ml-4">
                          {review.reviewBody}
                      </div>
                      <div className="review-reactions">
                      <Reactions
                        likes={review.likes}
                        dislikes={review.dislikes}
                        reviewId={review.reviewId}
                        fetchReviews={fetchReviews}
                        refreshData={refreshData}
                        userId={decodedToken ? decodedToken.appUserId : null}
                        token={token}
                      />
                      </div>
                      
                    
                  
                    </div>
                    {decodedToken && review.userId === decodedToken.appUserId && (
                        <div className="review-edit-delete">
                          <Button onClick={() => handleOpenModal(review.reviewId)} className="mr-4 mb-2 mt-4">Edit</Button>
                          <Button
                            className="mr-4 mb-2 mt-4"
                            variant="danger"
                            onClick={() => handleDelete(review.reviewId)}
                          >
                            Delete
                          </Button>
                        </div>
                      )}
                </div>
              ))}
              </div>
              
            </section>
          ) : (
            <h4 className="mb-4">No reviews available.</h4>
          )}
          {showModal && (
                <ReviewForm
                  fetchReviews={fetchReviews}
                  refreshData={refreshData}
                  reviewId={currentReviewId}
                  onClose={handleCloseModal}
                />
              )}
        </>
      );
      
}

export default ReviewList;





