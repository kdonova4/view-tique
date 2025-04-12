import { useEffect, useState } from "react";
import { Link, useParams } from "react-router-dom";

function TopGames() {

    const [topGames, setTopGames] = useState([])
    const [isFetching, setIsFetching] = useState(true);

    const { genreId, limit } = useParams();
    const url = `http://3.13.229.3:8080/api/v1/games`
    useEffect(() => {
        setIsFetching(true);

        fetch(`${url}/${genreId}/${limit}`)
        .then(response => {
            if(response.status === 200) {
                return response.json();
            } else {
                return Promise.reject(`Unexepected Status Code ${response.status}`)
            }
        })
        .then(data => setTopGames(data))
        .catch(console.log)
        .finally(() => {
            setIsFetching(false);
        })
        
    }, [genreId, limit])




    return(
        <>
            {!isFetching &&(
                topGames.length > 0  ? (
                    <section>
                                    
                                    <Link className='btn btn-outline-success' to={'/game/add'}>Add a Game</Link>
                                    <table className='table table-striped'>
                                        <thead className='thead-dark'>
                                            <tr>
                                                <th>Title</th>
                                                <th>Description</th>
                                                <th>Release Date</th>
                                                <th>User Score</th>
                                                <th>Critic Score</th>
                                                <th>User Review Count</th>
                                                <th>Critic Review Count</th>
                                                <th>Developer</th>
                                                <th>&nbsp;</th>
                                            </tr>
                                        </thead>
                                        <tbody>
                                            {topGames.map(game => (
                                                <tr key={game.gameId}>
                                                    <td><Link to={`/games/${game.gameId}`}>{game.title}</Link></td>
                                                    <td>{game.description}</td>
                                                    <td>{game.releaseDate}</td>
                                                    <td>{game.avgUserScore}</td>
                                                    <td>{game.avgCriticScore}</td>
                                                    <td>{game.userReviewCount}</td>
                                                    <td>{game.criticReviewCount}</td>
                                                    <td>{game.developer.developerName}</td>
                                                </tr>
                                            ))}
                                        </tbody>
                                    </table>
                                </section>
                ) : (
                    <p>No games</p>
                )
            )}
        </>
    )
}

export default TopGames;