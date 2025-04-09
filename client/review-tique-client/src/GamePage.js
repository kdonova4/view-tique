import { use, useEffect, useState } from "react";
import { useLocation, useNavigate, useParams } from "react-router-dom";
import ReviewList from "./ReviewList";
import { useWishlist } from "./context/WishlistContext";
import { useAuth } from "./context/AuthContext";
import { jwtDecode } from "jwt-decode";
import { Button } from "react-bootstrap";
import ImageComponent from "./ImageComponent";
import ReactShowMoreText from "react-show-more-text";

const GAME_DEFAULT = {
    title: "None",
    description: "None",
    releaseDate: "Unknown",
    avgUserScore: 0.0,
    avgCriticScore: 0.0,
    userReviewCount: 0,
    criticReviewCount: 0,
    developer:
    {
        developerId: null,
        developerName: "Unknown Developer"
    },
    genres: [],
    platforms: []
}

function GamePage() {
    const [game, setGame] = useState(null);
    const url = 'http://localhost:8080/v1/api/games'
    const [fetching, setFetching] = useState(true);
    const { gameId } = useParams();
    const { token } = useAuth();
    const location = useLocation();
    const decodedToken = token ? jwtDecode(token) : null;
    const { wishlist, addToWishlist, removeFromWishlist } = useWishlist();



    useEffect(() => {
        console.log("FIRSIT")
        setFetching(true);
        fetch(`${url}/${gameId}`)
            .then(response => {
                if (response.status === 200) {
                    return response.json();
                } else if (response.status === 404) {
                    console.error("Game Not Found");

                } else {
                    return Promise.reject(`Unexpected Status Code ${response.status}`)
                }
            })
            .then(data => setGame(data))
            .catch(console.log)
            .finally(() => {
                setFetching(false);
            })
    }, [gameId]);

    useEffect(() => {
        console.log("RUNNING")

    }, [wishlist, gameId])

    const refreshData = () => {

        console.log('REFRESHING')
        fetch(`${url}/${gameId}`)
            .then(response => {
                if (response.status === 200) {
                    return response.json();
                } else if (response.status === 404) {
                    console.error("Game Not Found");

                } else {
                    return Promise.reject(`Unexpected Status Code ${response.status}`)
                }
            })
            .then(data => setGame(data))
            .catch(console.log)
    }

    const isWishlisted = wishlist.some(item => Number(item.gameId) === Number(gameId))
    console.log("Wishlist:", wishlist);
    console.log("Current gameId:", gameId);
    console.log("isWishlisted:", isWishlisted);
    // return (
    //     <>

    //         {token && (
    //             !isWishlisted ?  (
    //                 <>

    //                 <Button variant="success" onClick={() => addToWishlist(gameId)}>
    //                     {"Add To Wishlist"}
    //                 </Button></>
    //             ) : (
    //                 <>
    //                 <Button variant="danger" onClick={() => removeFromWishlist(gameId)}>
    //                     {"Remove From Wishlist"}
    //                 </Button>
    //                 </>
    //             )
    //         )}




    //         <h1>Game Details</h1>
    //         {!fetching && (
    //             game ? ( // Open conditional check
    //                 <>
    //                 <div className="game-page-cover">
    //                     <img src={game.cover}></img>
    //                 </div>



    //                     <section>
    //                         <table className="table table-striped">
    //                             <thead className="thead-dark">
    //                                 <tr>
    //                                     <th>Title</th>
    //                                     <th>Description</th>
    //                                     <th>Release Date</th>
    //                                     <th>User Score</th>
    //                                     <th>Critic Score</th>
    //                                     <th>User Review Count</th>
    //                                     <th>Critic Review Count</th>
    //                                     <th>Developer</th>
    //                                 </tr>
    //                             </thead>
    //                             <tbody>
    //                                 <tr>
    //                                     <td>{game.title}</td>
    //                                     <td>{game.description}</td>
    //                                     <td>{game.releaseDate}</td>
    //                                     <td>{game.avgUserScore}</td>
    //                                     <td>{game.avgCriticScore}</td>
    //                                     <td>{game.userReviewCount}</td>
    //                                     <td>{game.criticReviewCount}</td>
    //                                     <td>{game.developer.developerName}</td>
    //                                 </tr>
    //                             </tbody>
    //                         </table>

    //                         <section>
    //                             <h5 className="mt-4">Genres:</h5>
    //                             {game.genres.length > 0 ? (
    //                                 <ul>
    //                                     {game.genres.map((item, index) => (
    //                                         <li key={index}>{item.genreName}</li>
    //                                     ))}
    //                                 </ul>
    //                             ) : (
    //                                 <p>No Genres Available</p>
    //                             )}
    //                         </section>

    //                         <div className="mb-4">
    //                             <h5 className="mt-4">Platforms:</h5>
    //                             {game.platforms.length > 0 ? (
    //                                 <ul>
    //                                     {game.platforms.map((item, index) => (
    //                                         <li key={index}>{item.platformName}</li>
    //                                     ))}
    //                                 </ul>
    //                             ) : (
    //                                 <p>No Platforms Available</p>
    //                             )}
    //                         </div>
    //                     </section>

    //                     <div><ReviewList refreshData={refreshData}/></div>
    //                 </>
    //             ) : ( // Else condition
    //                 <p>No Game Available</p>
    //             )
    //         )}

    //     </>
    // );


    const getRatingColor = (rating) => {
        if (rating >= 8) return '#15ff0073';
        if (rating >= 6) return '#fffb0073';
        if (rating >= 5) return '#ff910073';
        return '#ff000073';
    };

    return (
        <>

            {!fetching && game ? (
                <div className="game-container">
                    <div className="game-page">
                        <div className="game-info">
                            <header className="game-image-header" style={{ "--bg-url": `url(${game.cover.replace("t_cover_big", "t_1080p")})` }}>
                                <div className="container game-info-media"> {/* background for this should be large image of game */}
                                    <div className="game-info-container">
                                        <div className="game-info-root">
                                            <div className="game-title">
                                                <h1>{game.title}</h1>
                                            </div>
                                            <div className="game-info-small">
                                                <div className="release-date">
                                                    <span>{new Date(game.releaseDate).toLocaleString(undefined, {
                                                        year: 'numeric',
                                                        month: 'long',
                                                        day: 'numeric'
                                                    })}</span>
                                                </div>
                                                <div className="game-developer">
                                                    <span>{game.developer.developerName}</span>
                                                </div>
                                            </div>
                                            <div className="review-info-media">
                                                <div className="game-cover">
                                                    <div className="game-cover-img">
                                                        <img className="cover-img" src={game.cover}></img>
                                                    </div>
                                                </div>
                                                <div className="rating-container">
                                                    <div className="rating-root">
                                                        <div className="user-rating">
                                                            <div>
                                                                <div>
                                                                    User Score
                                                                </div>
                                                            </div>
                                                            <div>
                                                                <div className="circle-placeholder" style={{ backgroundColor: getRatingColor(game.avgUserScore) }} >
                                                                    <h2>{game.avgUserScore}</h2>
                                                                </div>
                                                            </div>
                                                            <div>
                                                                <div>
                                                                    {game.userReviewCount} User reviews
                                                                </div>
                                                            </div>
                                                        </div>
                                                        <div className="critic-rating">
                                                            <div>
                                                                <div>
                                                                    Critic Score
                                                                </div>
                                                            </div>
                                                            <div>
                                                                <div className="circle-placeholder" style={{ backgroundColor: getRatingColor(game.avgCriticScore) }}>
                                                                    <h2>{game.avgCriticScore}</h2>
                                                                </div>
                                                            </div>
                                                            <div>
                                                                <div>
                                                                    {game.criticReviewCount} Critic reviews
                                                                </div>
                                                            </div>
                                                        </div>
                                                    </div>
                                                </div>
                                            </div>
                                        </div>
                                    </div>
                                </div>
                            </header>
                            <div className="game-details-container">
                                <div>
                                    <div>
                                        <div className="container game-details">
                                            <div className="genre-wishlist">
                                                <p className="mt-4 genre-list">
                                                <strong>Genres: </strong>
                                                {game.genres.length > 0 ? (
                                                    <>


                                                        {game.genres.map((item, index) => (
                                                            <span key={index}>{item.genreName}{index < game.genres.length - 1 && ", "}&nbsp;</span>

                                                        ))}

                                                    </>

                                                ) : (
                                                    <p>No Genres Available</p>
                                                )}
                                            </p>
                                            <div className="wishlist-section">
                                                    {token && (
                                                !isWishlisted ? (
                                                    <>

                                                        <Button className="add-wishlist" variant="success" onClick={() => addToWishlist(gameId)}>
                                                            {"Add To Wishlist"}
                                                        </Button></>
                                                ) : (
                                                    <>
                                                        <Button className="remove-wishlist" variant="danger" onClick={() => removeFromWishlist(gameId)}>
                                                            {"Remove From Wishlist"}
                                                        </Button>
                                                    </>
                                                )
                                            )}
                                                </div>
                                            </div>
                                            
                                            <p>
                                                
                                                <strong>Platforms: </strong>
                                                {game.platforms.length > 0 ? (
                                                    <>


                                                        {game.platforms.map((item, index) => (
                                                            <span key={index}>{item.platformName}{index < game.platforms.length - 1 && ", "}&nbsp;</span>

                                                        ))}

                                                    </>

                                                ) : (
                                                    <p>No Platforms Available</p>
                                                )}
                                            </p>


                                            <ReactShowMoreText
                                                lines={4}
                                                more="Show More"
                                                lest="Show Less"
                                                anchorClass="show-more btn-link p-0"
                                                expanded={false}
                                                truncatedEndingComponent={"... "}
                                            >

                                                <p>{game.description}</p>
                                            </ReactShowMoreText>
                                                
                                            
                                        </div>
                                    </div>
                                </div>
                            </div>
                        </div>
                        <div className="reviews">
                            <header className="container review-header">
                                <div>
                                    <h2>Reviews</h2>
                                </div>
                            </header>
                        </div>
                        <div className=" container reviews">
                            <ReviewList refreshData={refreshData} />
                        </div>

                    </div>
                </div>
            ) : (
                <div>No Game Available</div>
            )}


        </>
    );
}

export default GamePage;