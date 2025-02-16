import { useEffect, useState } from "react";
import { useParams } from "react-router-dom";

function ReviewList(){

    const [reviews, setReviews] = useState([]);
    const url = 'http://localhost:8080/v1/api/reviews/game'


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

    return(<>
        <h1>Reviews List</h1>
        <section>
                <h2 className='mb-4'>Reviews</h2>
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
                                    </tr>
                                ))}
                            </tbody>
                        </table>
                    </section>
    </>)
}

export default ReviewList;