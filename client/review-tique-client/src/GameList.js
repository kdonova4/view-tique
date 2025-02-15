import { useEffect, useState } from "react";
import { Link } from "react-router-dom";

function GameList(){
    // STATE
    const [games, setGames] = useState([]);
    const url = 'http://localhost:8080/v1/api/games'

    // useEffect
    useEffect(() => {
        fetch(url)
        .then(response => {
            if(response.status === 200) {
                return response.json();
            }else{
                return Promise.reject(`Unexpected Status Code ${response.status}`)
            }
        })
        .then(data => setGames(data))
        .catch(console.log);
    }, []); // call me once on page load
    
    // handle delete
    const handleDeleteGame = (gameId) => {
        const game = games.find(g => g.gameId === gameId);
        if(window.confirm(`Delete Game: ${game.title}?`)) {
            const init = {
                method: 'DELETE'
            };
            fetch(`${url}/${gameId}`, init)
            .then(response => {
                if(response.status === 204){
                    // create a copy of the array remove the game
                    const newGames = games.filter(g => g.gameId !== gameId)
                    // update the game state
                    setGames(newGames);
                } else{
                    return Promise.reject(`Unexpected Status Code ${response.status}`)
                }
            })
            .catch(console.log);
        }
    }

    return(<>
        <section>
                <h2 className='mb-4'>Games</h2>
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
                        {games.map(game => (
                            <tr key={game.gameId}>
                                <td>{game.title}</td>
                                <td>{game.description}</td>
                                <td>{game.releaseDate}</td>
                                <td>{game.avgUserScore}</td>
                                <td>{game.avgCriticScore}</td>
                                <td>{game.userReviewCount}</td>
                                <td>{game.criticReviewCount}</td>
                                <td>{game.developer.developerName}</td>
                                <td>
                                    <Link className='btn btn-outline-warning mr-4' to={`/game/edit/${game.gameId}`}>Update</Link>
                                    <button className='btn btn-outline-danger mr-4' onClick={() => handleDeleteGame(game.gameId)}>Delete</button>
                                </td>
                            </tr>
                        ))}
                    </tbody>
                </table>
            </section>
    </>);
}

export default GameList;