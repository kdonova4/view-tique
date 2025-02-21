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

    const handleDelete = (reviewId) => {
        if(window.confirm(`Delete Review`)) {
            const headers = {
                'Authorization': `Bearer ${token}`,
                'Content-Type': 'application/json'
            }
            const init = {
                method: 'DELETE',
                headers: headers
            }

            fetch(`http://localhost:8080/v1/api/reviews/${reviewId}`, init)
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



    return (
        <>
          <h2 className='mb-4'>Reviews</h2>
          <Button
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
            Add Review
          </Button>
      
          {Array.isArray(reviews) && reviews.length > 0 ? (
            <section>
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
                  {reviews.map((review) => (
                    <tr key={review.reviewId}>
                      <td>{review.score}</td>
                      <td>
                        {new Date(review.timestamp).toLocaleString(undefined, {
                          year: 'numeric',
                          month: 'long',
                          day: 'numeric',
                          hour: 'numeric',
                          minute: '2-digit',
                        })}
                      </td>
                      <td>{review.reviewBody}</td>
                      <Reactions
                        likes={review.likes}
                        dislikes={review.dislikes}
                        reviewId={review.reviewId}
                        fetchReviews={fetchReviews}
                        refreshData={refreshData}
                        userId={decodedToken ? decodedToken.appUserId : null}
                        token={token}
                      />
                      
      
                      {decodedToken && review.userId === decodedToken.appUserId && (
                        <td>
                          <Button onClick={() => handleOpenModal(review.reviewId)}>Edit</Button>
                          <Button
                            className="ml-4"
                            variant="danger"
                            onClick={() => handleDelete(review.reviewId)}
                          >
                            Delete
                          </Button>
                        </td>
                      )}
                    </tr>
                  ))}
                </tbody>
              </table>
      
              
            </section>
          ) : (
            <p>No reviews available.</p>
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