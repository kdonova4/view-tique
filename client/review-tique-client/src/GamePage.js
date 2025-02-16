import { useEffect, useState } from "react";
import { useNavigate, useParams } from "react-router-dom";
import ReviewList from "./ReviewList";

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

function GamePage(){
    const [game, setGame] = useState(null);
    const url = 'http://localhost:8080/v1/api/games'
    const [fetching, setFetching] = useState(true);
    const { gameId } = useParams();

    useEffect(() => {
        setFetching(true);
        fetch(`${url}/${gameId}`)
        .then(response => {
            if(response.status === 200){
                return response.json();
            }else if (response.status === 404){
                console.error("Game Not Found");
                
            }else{
                return Promise.reject(`Unexpected Status Code ${response.status}`)
            }
        })
        .then(data => setGame(data))
        .catch(console.log)
        .finally(() =>{
            setFetching(false);
        })
    }, [gameId]);

    return (
        <>
            <h1>Game Details List</h1>
            {!fetching && (
                game ? ( // Open conditional check
                    <>
                        <section>
                            <h2 className="mb-4">Reviews</h2>
                            <table className="table table-striped">
                                <thead className="thead-dark">
                                    <tr>
                                        <th>Title</th>
                                        <th>Description</th>
                                        <th>Release Date</th>
                                        <th>User Score</th>
                                        <th>Critic Score</th>
                                        <th>User Review Count</th>
                                        <th>Critic Review Count</th>
                                        <th>Developer</th>
                                    </tr>
                                </thead>
                                <tbody>
                                    <tr>
                                        <td>{game.title}</td>
                                        <td>{game.description}</td>
                                        <td>{game.releaseDate}</td>
                                        <td>{game.avgUserScore}</td>
                                        <td>{game.avgCriticScore}</td>
                                        <td>{game.userReviewCount}</td>
                                        <td>{game.criticReviewCount}</td>
                                        <td>{game.developer.developerName}</td>
                                    </tr>
                                </tbody>
                            </table>
        
                            <section>
                                <h5 className="mt-4">Genres:</h5>
                                {game.genres.length > 0 ? (
                                    <ul>
                                        {game.genres.map((item, index) => (
                                            <li key={index}>{item.genreName}</li>
                                        ))}
                                    </ul>
                                ) : (
                                    <p>No Genres Available</p>
                                )}
                            </section>
        
                            <div className="mb-4">
                                <h5 className="mt-4">Platforms:</h5>
                                {game.platforms.length > 0 ? (
                                    <ul>
                                        {game.platforms.map((item, index) => (
                                            <li key={index}>{item.platformName}</li>
                                        ))}
                                    </ul>
                                ) : (
                                    <p>No Platforms Available</p>
                                )}
                            </div>
                        </section>
        
                        <div><ReviewList/></div>
                    </>
                ) : ( // Else condition
                    <p>No Game Available</p>
                )
            )}
            
        </>
    );

}

export default GamePage;