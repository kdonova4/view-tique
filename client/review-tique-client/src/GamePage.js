import { use, useEffect, useState } from "react";
import { useLocation, useNavigate, useParams } from "react-router-dom";
import ReviewList from "./ReviewList";
import { useWishlist } from "./context/WishlistContext";
import { useAuth } from "./context/AuthContext";
import { jwtDecode } from "jwt-decode";
import { Button } from "react-bootstrap";
import ImageComponent from "./ImageComponent";

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


    return (
        <>



            <div className="game-container">
                <div className="game-page">
                    <div className="game-info">
                        <header className="game-image-header">
                            <div className="container game-info-media"> {/* background for this should be large image of game */}
                                <div className="game-info-container">
                                    <div className="game-info-root">
                                        <div className="game-title">
                                            <h1>Game Title</h1>
                                        </div>
                                        <div className="game-info-small">
                                            <div className="release-date">
                                                <span>Release Date</span>
                                            </div>
                                            <div className="game-developer">
                                                <span>Developer</span>
                                            </div>
                                        </div>
                                        <div className="review-info-media">
                                            <div className="game-cover">
                                                <div>
                                                    <img src="https://images.igdb.com/igdb/image/upload/t_cover_big/co25jt.webp"></img>
                                                </div>
                                            </div>
                                            <div className="rating-container">
                                                <div className="rating-root">
                                                    <div className="user-rating">
                                                        <div>
                                                            <div>
                                                                User
                                                            </div>
                                                        </div>
                                                    </div>
                                                    <div className="critic-rating">
                                                        <div>
                                                            <div>
                                                                Critic
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
                        <div className="container game-details">
                            <div>
                                <div>
                                    <div>
                                        <strong>Genres: </strong>
                                        <strong>Platforms: </strong>

                                        <p>Godfather ipsum dolor sit amet. Only don't tell me you're innocent. Because it insults my intelligence and makes me very angry. You talk about vengeance. Is vengeance going to bring your son back to you? Or my boy to me? Mr Corleone is Johnny Fontane's godfather. Now Italians regard that as a very close, a very sacred religious relationship. Why do you hurt me, Michael? I've always been loyal to you. Just when I thought I was out... they pull me back in.</p>
                                    </div>
                                </div>
                            </div>
                        </div>
                        </div>
                        <div className="review-select">
                            <header className="container review-header">
                                <div>
                                    User Reviews
                                </div>
                                <div>
                                    Critic Reviews
                                </div>
                            </header>
                        </div>
                        <div className=" container reviews">
                            Godfather ipsum dolor sit amet. Only don't tell me you're innocent. Because it insults my intelligence and makes me very angry. You talk about vengeance. Is vengeance going to bring your son back to you? Or my boy to me? Mr Corleone is Johnny Fontane's godfather. Now Italians regard that as a very close, a very sacred religious relationship. Why do you hurt me, Michael? I've always been loyal to you. Just when I thought I was out... they pull me back in.Godfather ipsum dolor sit amet. Only don't tell me you're innocent. Because it insults my intelligence and makes me very angry. You talk about vengeance. Is vengeance going to bring your son back to you? Or my boy to me? Mr Corleone is Johnny Fontane's godfather. Now Italians regard that as a very close, a very sacred religious relationship. Why do you hurt me, Michael? I've always been loyal to you. Just when I thought I was out... they pull me back in.Godfather ipsum dolor sit amet. Only don't tell me you're innocent. Because it insults my intelligence and makes me very angry. You talk about vengeance. Is vengeance going to bring your son back to you? Or my boy to me? Mr Corleone is Johnny Fontane's godfather. Now Italians regard that as a very close, a very sacred religious relationship. Why do you hurt me, Michael? I've always been loyal to you. Just when I thought I was out... they pull me back in.Godfather ipsum dolor sit amet. Only don't tell me you're innocent. Because it insults my intelligence and makes me very angry. You talk about vengeance. Is vengeance going to bring your son back to you? Or my boy to me? Mr Corleone is Johnny Fontane's godfather. Now Italians regard that as a very close, a very sacred religious relationship. Why do you hurt me, Michael? I've always been loyal to you. Just when I thought I was out... they pull me back in.Godfather ipsum dolor sit amet. Only don't tell me you're innocent. Because it insults my intelligence and makes me very angry. You talk about vengeance. Is vengeance going to bring your son back to you? Or my boy to me? Mr Corleone is Johnny Fontane's godfather. Now Italians regard that as a very close, a very sacred religious relationship. Why do you hurt me, Michael? I've always been loyal to you. Just when I thought I was out... they pull me back in.Godfather ipsum dolor sit amet. Only don't tell me you're innocent. Because it insults my intelligence and makes me very angry. You talk about vengeance. Is vengeance going to bring your son back to you? Or my boy to me? Mr Corleone is Johnny Fontane's godfather. Now Italians regard that as a very close, a very sacred religious relationship. Why do you hurt me, Michael? I've always been loyal to you. Just when I thought I was out... they pull me back in.Godfather ipsum dolor sit amet. Only don't tell me you're innocent. Because it insults my intelligence and makes me very angry. You talk about vengeance. Is vengeance going to bring your son back to you? Or my boy to me? Mr Corleone is Johnny Fontane's godfather. Now Italians regard that as a very close, a very sacred religious relationship. Why do you hurt me, Michael? I've always been loyal to you. Just when I thought I was out... they pull me back in.Godfather ipsum dolor sit amet. Only don't tell me you're innocent. Because it insults my intelligence and makes me very angry. You talk about vengeance. Is vengeance going to bring your son back to you? Or my boy to me? Mr Corleone is Johnny Fontane's godfather. Now Italians regard that as a very close, a very sacred religious relationship. Why do you hurt me, Michael? I've always been loyal to you. Just when I thought I was out... they pull me back in.Godfather ipsum dolor sit amet. Only don't tell me you're innocent. Because it insults my intelligence and makes me very angry. You talk about vengeance. Is vengeance going to bring your son back to you? Or my boy to me? Mr Corleone is Johnny Fontane's godfather. Now Italians regard that as a very close, a very sacred religious relationship. Why do you hurt me, Michael? I've always been loyal to you. Just when I thought I was out... they pull me back in.Godfather ipsum dolor sit amet. Only don't tell me you're innocent. Because it insults my intelligence and makes me very angry. You talk about vengeance. Is vengeance going to bring your son back to you? Or my boy to me? Mr Corleone is Johnny Fontane's godfather. Now Italians regard that as a very close, a very sacred religious relationship. Why do you hurt me, Michael? I've always been loyal to you. Just when I thought I was out... they pull me back in.Godfather ipsum dolor sit amet. Only don't tell me you're innocent. Because it insults my intelligence and makes me very angry. You talk about vengeance. Is vengeance going to bring your son back to you? Or my boy to me? Mr Corleone is Johnny Fontane's godfather. Now Italians regard that as a very close, a very sacred religious relationship. Why do you hurt me, Michael? I've always been loyal to you. Just when I thought I was out... they pull me back in.Godfather ipsum dolor sit amet. Only don't tell me you're innocent. Because it insults my intelligence and makes me very angry. You talk about vengeance. Is vengeance going to bring your son back to you? Or my boy to me? Mr Corleone is Johnny Fontane's godfather. Now Italians regard that as a very close, a very sacred religious relationship. Why do you hurt me, Michael? I've always been loyal to you. Just when I thought I was out... they pull me back in.Godfather ipsum dolor sit amet. Only don't tell me you're innocent. Because it insults my intelligence and makes me very angry. You talk about vengeance. Is vengeance going to bring your son back to you? Or my boy to me? Mr Corleone is Johnny Fontane's godfather. Now Italians regard that as a very close, a very sacred religious relationship. Why do you hurt me, Michael? I've always been loyal to you. Just when I thought I was out... they pull me back in.Godfather ipsum dolor sit amet. Only don't tell me you're innocent. Because it insults my intelligence and makes me very angry. You talk about vengeance. Is vengeance going to bring your son back to you? Or my boy to me? Mr Corleone is Johnny Fontane's godfather. Now Italians regard that as a very close, a very sacred religious relationship. Why do you hurt me, Michael? I've always been loyal to you. Just when I thought I was out... they pull me back in.Godfather ipsum dolor sit amet. Only don't tell me you're innocent. Because it insults my intelligence and makes me very angry. You talk about vengeance. Is vengeance going to bring your son back to you? Or my boy to me? Mr Corleone is Johnny Fontane's godfather. Now Italians regard that as a very close, a very sacred religious relationship. Why do you hurt me, Michael? I've always been loyal to you. Just when I thought I was out... they pull me back in.Godfather ipsum dolor sit amet. Only don't tell me you're innocent. Because it insults my intelligence and makes me very angry. You talk about vengeance. Is vengeance going to bring your son back to you? Or my boy to me? Mr Corleone is Johnny Fontane's godfather. Now Italians regard that as a very close, a very sacred religious relationship. Why do you hurt me, Michael? I've always been loyal to you. Just when I thought I was out... they pull me back in.Godfather ipsum dolor sit amet. Only don't tell me you're innocent. Because it insults my intelligence and makes me very angry. You talk about vengeance. Is vengeance going to bring your son back to you? Or my boy to me? Mr Corleone is Johnny Fontane's godfather. Now Italians regard that as a very close, a very sacred religious relationship. Why do you hurt me, Michael? I've always been loyal to you. Just when I thought I was out... they pull me back in.Godfather ipsum dolor sit amet. Only don't tell me you're innocent. Because it insults my intelligence and makes me very angry. You talk about vengeance. Is vengeance going to bring your son back to you? Or my boy to me? Mr Corleone is Johnny Fontane's godfather. Now Italians regard that as a very close, a very sacred religious relationship. Why do you hurt me, Michael? I've always been loyal to you. Just when I thought I was out... they pull me back in.Godfather ipsum dolor sit amet. Only don't tell me you're innocent. Because it insults my intelligence and makes me very angry. You talk about vengeance. Is vengeance going to bring your son back to you? Or my boy to me? Mr Corleone is Johnny Fontane's godfather. Now Italians regard that as a very close, a very sacred religious relationship. Why do you hurt me, Michael? I've always been loyal to you. Just when I thought I was out... they pull me back in.Godfather ipsum dolor sit amet. Only don't tell me you're innocent. Because it insults my intelligence and makes me very angry. You talk about vengeance. Is vengeance going to bring your son back to you? Or my boy to me? Mr Corleone is Johnny Fontane's godfather. Now Italians regard that as a very close, a very sacred religious relationship. Why do you hurt me, Michael? I've always been loyal to you. Just when I thought I was out... they pull me back in.Godfather ipsum dolor sit amet. Only don't tell me you're innocent. Because it insults my intelligence and makes me very angry. You talk about vengeance. Is vengeance going to bring your son back to you? Or my boy to me? Mr Corleone is Johnny Fontane's godfather. Now Italians regard that as a very close, a very sacred religious relationship. Why do you hurt me, Michael? I've always been loyal to you. Just when I thought I was out... they pull me back in.Godfather ipsum dolor sit amet. Only don't tell me you're innocent. Because it insults my intelligence and makes me very angry. You talk about vengeance. Is vengeance going to bring your son back to you? Or my boy to me? Mr Corleone is Johnny Fontane's godfather. Now Italians regard that as a very close, a very sacred religious relationship. Why do you hurt me, Michael? I've always been loyal to you. Just when I thought I was out... they pull me back in.Godfather ipsum dolor sit amet. Only don't tell me you're innocent. Because it insults my intelligence and makes me very angry. You talk about vengeance. Is vengeance going to bring your son back to you? Or my boy to me? Mr Corleone is Johnny Fontane's godfather. Now Italians regard that as a very close, a very sacred religious relationship. Why do you hurt me, Michael? I've always been loyal to you. Just when I thought I was out... they pull me back in.Godfather ipsum dolor sit amet. Only don't tell me you're innocent. Because it insults my intelligence and makes me very angry. You talk about vengeance. Is vengeance going to bring your son back to you? Or my boy to me? Mr Corleone is Johnny Fontane's godfather. Now Italians regard that as a very close, a very sacred religious relationship. Why do you hurt me, Michael? I've always been loyal to you. Just when I thought I was out... they pull me back in.
                        </div>
                    
                </div>
            </div>
        </>
    );
}

export default GamePage;