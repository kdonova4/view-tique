import { use, useCallback, useEffect, useState } from "react";
import { useAuth } from "./context/AuthContext";
import { jwtDecode } from "jwt-decode";
import { Link } from "react-router-dom";
import { Button } from "react-bootstrap";

function UserProfile() {

    const [reviews, setReviews] = useState([]);
    const [games, setGames] = useState([]);
    const [wishlist, setWishlist] = useState([]);
    const [loadingReviews, setLoadingReviews] = useState(true)
    const [loadingWishlist, setLoadingWishlist] = useState(true)
    const { token, role } = useAuth();
    const decodedToken = token ? jwtDecode(token) : null;
    const username = decodedToken?.sub;
    const baseUrl = 'http://localhost:8080/v1/api'

    const fetchData = useCallback( async (type) => {
            if(type === "reviews") {
                try {
                    const response = await fetch(`${baseUrl}/reviews/me` , {
                        headers: {
                            Authorization: `Bearer ${token}`,
                            "Content-Type": "application/json"
                        }
                    });

                    if(!response.ok) {
                        throw new Error(`Unexpected Status Code: ${response.status}`)
                    }

                    const data = await response.json();
                    setReviews(data);
                }catch(error) {
                    console.error(error);
                } finally {
                    setLoadingReviews(false)
                }
            } else if(type === "wishlist"){
                try {
                    const response = await fetch(`${baseUrl}/wishlists/wishlist` , {
                        headers: {
                            Authorization: `Bearer ${token}`,
                            "Content-Type": "application/json"
                        }
                    });

                    if(!response.ok) {
                        throw new Error(`Unexpected Status Code: ${response.status}`)
                    }

                    const data = await response.json();
                    setWishlist(data);
                }catch(error) {
                    console.error(error);
                } finally {
                    setLoadingWishlist(false)
                }
            }
        }, [token])

    const fetchGame = async (gameId) => {
        if(!games[gameId]) {
            try {
                    const response = await fetch(`${baseUrl}/games/${gameId}`);
                    const gameData = await response.json();

                    setGames((prevGames) => ({
                        ...prevGames,
                        [gameId]: gameData, 
                    }));
            }catch(error) {
                console.error(error);
            }
        }
    }
    console.log(wishlist)
    useEffect(() => {
        fetchData("reviews");
        fetchData("wishlist");
    }, [fetchData])

    useEffect(() => {
        reviews.forEach((review) => {
            if (!games[review.gameId]) { // If the game isn't already fetched
                fetchGame(review.gameId);
            }
        });
    }, [reviews])

    useEffect(() => {
        wishlist.forEach((wishlist) => {
            if (!games[wishlist.gameId]) { // If the game isn't already fetched
                fetchGame(wishlist.gameId);
            }
        });
    }, [wishlist])

    const handleDeleteWishlistGame = (wishlistId) => {
        if(window.confirm(`Remove Game From Wishlist?`)) {
            const headers = {
                'Authorization': `Bearer ${token}`,
                'Content-Type': 'application/json'
            }
            const init = {
                method: 'DELETE',
                headers: headers
            }

            fetch(`http://localhost:8080/v1/api/wishlists/${wishlistId}`, init)
                .then(response => {
                    if(response.status === 204) {
                        const newWishlist = wishlist.filter(wishlistGame => wishlistGame.wishlistId !== wishlist);
                        setWishlist(newWishlist);
                        fetchData("wishlist")
                    } else {
                        return Promise.reject(`Unexpected Status Code ${response.status}`)
                    }
                })
                .catch(console.log)
        }
    }

    return (
        <>
        <section>
        <h1>{username}</h1>
        <h2>{role === "ROLE_USER" ? "User" : role === "ROLE_CRITIC" ? "Critic" : "Unknown Role"}</h2>
            {!loadingReviews && (
                <>
                    <h2>User Reviews</h2>
                    {reviews.length === 0 ? (
                        <p>No Reviews Found For This User</p>
                    ) : (
                        reviews.map((review) => (
                            <div key={review.reviewId}>
                                <Link to={`/games/${review.gameId}`}>{games[review.gameId]?.title || ""} </Link>
                                <h6>{review.score}</h6>
                                <h6>{review.reviewBody}</h6>
                            </div>
                        ))
                    )}
                </>
            )}
            </section>
            
            <section className="mt-6">
                {!loadingWishlist && (
                <>
                    
                    
                    <h2>Your Wishlist</h2>
                    {wishlist.length === 0 ? (
                        <p>There are no games on your wishlist</p>
                    ) : (
                        wishlist.map((wishlistGame) => (
                            <div key={wishlistGame.wishlistId}>
                                <Link to={`/games/${wishlistGame.gameId}`}>{games[wishlistGame.gameId]?.title || ""}</Link>
                                <h6>{games[wishlistGame.gameId]?.description || ""}</h6>
                                <Button
                                    className="ml-4"
                                    variant="danger"
                                    onClick={() => handleDeleteWishlistGame(wishlistGame.wishlistId)}
                                >
                                    Delete
                                </Button>
                            </div>
                        ))
                    )}
                </>
            )}
            </section>
        </>
    );
    
}

export default UserProfile;