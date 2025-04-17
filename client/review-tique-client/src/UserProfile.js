import { use, useCallback, useEffect, useState } from "react";
import { useAuth } from "./context/AuthContext";
import { jwtDecode } from "jwt-decode";
import { Link } from "react-router-dom";
import { Button } from "react-bootstrap";
import { useWishlist } from "./context/WishlistContext";

function UserProfile() {

    const [reviews, setReviews] = useState([]);
    const [games, setGames] = useState([]);
    const [loadingReviews, setLoadingReviews] = useState(true)
    const [loadingWishlist, setLoadingWishlist] = useState(false)
    const { token, role } = useAuth();
    const decodedToken = token ? jwtDecode(token) : null;
    const username = decodedToken?.sub;
    const baseUrl = 'http://localhost:8080/api/v1'
    const { wishlist, removeFromWishlist } = useWishlist();
    const fetchData = useCallback(async (type) => {
        if (type === "reviews") {
            try {
                const response = await fetch(`${baseUrl}/reviews/me`, {
                    headers: {
                        Authorization: `Bearer ${token}`,
                        "Content-Type": "application/json"
                    }
                });

                if (!response.ok) {
                    throw new Error(`Unexpected Status Code: ${response.status}`)
                }

                const data = await response.json();
                setReviews(data);
            } catch (error) {
                console.error(error);
            } finally {
                setLoadingReviews(false)
            }
        } else if (type === "wishlist") {

        }
    }, [token])

    const fetchGame = async (gameId) => {
        if (!games[gameId]) {
            try {
                const response = await fetch(`${baseUrl}/games/${gameId}`);
                const gameData = await response.json();

                setGames((prevGames) => ({
                    ...prevGames,
                    [gameId]: gameData,
                }));
            } catch (error) {
                console.error(error);
            }
        }
    }
    console.log(wishlist)
    useEffect(() => {
        fetchData("reviews");
    }, [fetchData])

    useEffect(() => {
        reviews.forEach((review) => {
            if (!games[review.gameId]) { // If the game isn't already fetched
                fetchGame(review.gameId);
            }
        });
    }, [reviews])

    useEffect(() => {
        wishlist.forEach((wishlistedItem) => {
            if (!games[wishlistedItem.gameId]) { // If the game isn't already fetched
                fetchGame(wishlistedItem.gameId);
            }
        });
    }, [wishlist])



    const getRatingColor = (rating) => {
        if (rating >= 8) return '#15ff0073';
        if (rating >= 6) return '#fffb0073';
        if (rating >= 5) return '#ff910073';
        return '#ff000073';
    };
    return (
        <>
            <section className="container">
                <h1>{username} - {role === "ROLE_USER" ? "User" : role === "ROLE_CRITIC" ? "Critic" : "Unknown Role"}</h1>

                {!loadingReviews && (
                    <>
                        <h2>User Reviews</h2>
                        <div className="container">
                            {reviews.length === 0 ? (
                                <p>No Reviews Found For This User</p>
                            ) : (
                                reviews.map((review) => (
                                    <div className="review-card mb-4" key={review.reviewId}>
                                        <div className="review-info mr-2">
                                            <div className="review-score ml-4 mt-4">
                                                <div className="circle-review mb-4" style={{ backgroundColor: getRatingColor(review.score) }}>{review.score}</div>
                                            </div>
                                            <div className="review-user mb-4"><Link to={`/games/${review.gameId}`}>{games[review.gameId]?.title || ""} </Link></div>
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
                                            </div>
                                        </div>
                                    </div>
                                ))
                            )}
                        </div>

                    </>
                )}
            </section>

            <section className="mt-6 container">
                {!loadingWishlist && (
                    <>


                        <h2>Your Wishlist</h2>
                        <div className="container">
                        {wishlist.length === 0 ? (
                            <p>There are no games on your wishlist</p>
                        ) : (
                            wishlist.map((wishlistGame) => (
                                <div className="review-card mb-4" key={wishlistGame.wishlistId}>
                                        <div className="review-info mr-2">
                                            <div className="review-user mb-4 ml-4 mt-4"><Link to={`/games/${wishlistGame.gameId}`}>{games[wishlistGame.gameId]?.title || ""}</Link></div>
                                            <div><Button
                                        className="ml-4 mb-4"
                                        variant="danger"
                                        onClick={() => removeFromWishlist(wishlistGame.gameId)}
                                    >
                                        Delete
                                    </Button></div>
                                        </div>
                                        
                                        <div className="review-body">
                                            <div className="review-text mb-2 mt-4 ml-4">
                                                {games[wishlistGame.gameId]?.description || ""}
                                            </div>
                                            <div className="review-reactions">
                                            
                                            </div>
                                        </div>
                                    </div>
                            ))
                        )}
                        </div>
                    </>
                )}
            </section>
        </>
    );

}

export default UserProfile;



{/* <Link to={`/games/${wishlistGame.gameId}`}>{games[wishlistGame.gameId]?.title || ""}</Link>
                                    <h6>{games[wishlistGame.gameId]?.description || ""}</h6>
                                    <Button
                                        className="ml-4"
                                        variant="danger"
                                        onClick={() => removeFromWishlist(wishlistGame.gameId)}
                                    >
                                        Delete
                                    </Button> */}