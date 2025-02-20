import { jwtDecode } from 'jwt-decode';
import { useEffect, useState } from 'react';
import Button from 'react-bootstrap/Button';
import Form from 'react-bootstrap/Form';
import Modal from 'react-bootstrap/Modal';
import { useNavigate, useParams } from 'react-router-dom';

const REVIEW_DEFAULT = {
  score: 0.0,
  timeStamp: '',
  reviewBody: '',
  likes: 0,
  dislikes: 0,
  userId: null,
  gameId: null,

};

function ReviewForm({ reviewId, onClose, refreshData, fetchReviews }) {
  const token = localStorage.getItem("token");
  let decodedToken;
  if (token) {
    decodedToken = jwtDecode(token);
  }

  const { gameId } = useParams();
  const appUserId = decodedToken.appUserId;
  const [review, setReview] = useState(REVIEW_DEFAULT);
  const [errors, setErrors] = useState([]);
  const url = 'http://localhost:8080/v1/api/reviews';

  const navigate = useNavigate();
useEffect(() => {
  if(reviewId) {
    fetch(`${url}/${reviewId}`)
    .then(response => {
      if(response.status === 200) {
        return response.json();
      }else{
        return Promise.reject(`Unexpected Status Code ${response.status}`)
      }
    })
    .then(data => {
      setReview(data);
    })
    .catch(console.log)
  } else if(gameId) {
    setReview(prevReview => ({
      ...prevReview,
      gameId: parseInt(gameId),
      userId: parseInt(decodedToken.appUserId)
    }));
  }
}, [reviewId])

  const handleSubmit = (event) => {
    event.preventDefault();
    if(reviewId) {
      updateReview();
    } else {
      addReview();
    }
  }

  const handleChange = (event) => {
    const newReview = {...review};
    newReview[event.target.name] = event.target.value
    setReview(newReview);
  }

  const addReview = () => {
    const init = {
      method: 'POST',
      headers: {
        'Content-Type': 'application/json',
        'Authorization': `Bearer ${token}`
      },
      body: JSON.stringify({
        ...review,
        userId: parseInt(decodedToken.appUserId)
      })
    }
    fetch(url, init)
    .then(response => {
      if(response.status === 201 || response.status === 400) {
        return response.json();
      } else {
        return Promise.reject(`Unexpected Status Code: ${response.status}`);
      }
    })
    .then(data => {
      if(!data.reviewId) {
        setErrors(data);
      } else {
        fetchReviews();
        refreshData();
        onClose();
      }
    })
    .catch(console.log);
  }

  const updateReview = () => {
    const init = {
      method: 'PUT',
      headers: {
        'Content-Type': 'application/json',
        'Authorization': `Bearer ${token}`
      },
      body: JSON.stringify({
        ...review,
        gameId: parseInt(gameId),
        userId: parseInt(decodedToken.appUserId)
      })
    }
    fetch(`${url}/${reviewId}`, init)
    .then(response => {
      if(response.status === 204) {
        return null;
      } else if(response.status === 400) {
        return response.json()
      } else {
        return Promise.reject(`Unexpected Status Code ${response.status}`)
      }
    })
    .then(data => {
      if(!data) {
        fetchReviews();
        refreshData();
        onClose();
      } else {
        setErrors(data);
      }
    })
    .catch(console.log)
  }
  return (
    <>


      <Modal show={onClose}>
        <Modal.Header>
          <Modal.Title>{reviewId ? "Edit Review" : "Add Review"}</Modal.Title>
        </Modal.Header>
        <Modal.Body>
          <Form>
            <Form.Group className="mb-3" controlId="exampleForm.ControlInput1">
              {errors.length > 0 && (
                <div className="alert alert-danger">
                <p>The following Errors were found:</p>
                <ul>
                    {errors.map(error => (
                        <li key={error}>{error}</li>
                    ))}
                </ul>
            </div>
              )}
              
              <Form.Control
                type="number"
                name="score"
                step="0.1"
                min="0.0"
                max="10.0"
                placeholder="0.0"
                value={review.score}
                onChange={handleChange}
                autoFocus
                required
              />
            </Form.Group>
            <Form.Group
              className="mb-3"
              controlId="exampleForm.ControlTextarea1"
            >
              <Form.Label>Review</Form.Label>
              <Form.Control
                as="textarea"
                name="reviewBody"
                value={review.reviewBody}
                onChange={handleChange}
                rows={14}
                required
              />
            </Form.Group>
          </Form>
        </Modal.Body>
        <Modal.Footer>
          <Button variant="secondary" onClick={onClose}>
            Close
          </Button>
          <Button variant="primary" onClick={handleSubmit}>
            {reviewId ? "Edit" : "Add"}
          </Button>
        </Modal.Footer>
      </Modal>
    </>
  )
}

export default ReviewForm;