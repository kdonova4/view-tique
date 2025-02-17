import { useEffect, useState } from "react";
import { Link, useLocation } from "react-router-dom";
import DeveloperFilter from "./DeveloperFilter";

function GameList(){
    // STATE
    const [games, setGames] = useState([]);
    const [gameName, setGameName] = useState('')
    const [genres, setGenres] = useState([]);
    const [platforms, setPlatforms] = useState([]);
    const [developerId, setDeveloperId] = useState('');
    const [fetching, setFetching] = useState(true);
    const url = 'http://localhost:8080/v1/api/games/search'
    const location = useLocation();
    const getQueryParams = () => {
        const urlParams = new URLSearchParams(location.search);

        return {
            gameName: urlParams.get('gameName') || '',
            genres: urlParams.get('genres') ? urlParams.get('genres').split(',') : [],
            platforms: urlParams.get('platforms') ? urlParams.get('platforms').split(',') : [],
            developerId: urlParams.get('developerId') || ''
        }
    }


    // useEffect
    useEffect(() => {

        const { gameName, genres, platforms, developerId } = getQueryParams();
        setFetching(true);
        setGameName(gameName);
        setGenres(genres);
        setPlatforms(platforms);
        setDeveloperId(developerId);

        const queryParams = new URLSearchParams();

        if(gameName) queryParams.append('gameName', gameName);
        if(genres.length > 0) queryParams.append('genres', genres.join(','));
        if(platforms.length > 0) queryParams.append('platforms', platforms.join(','));
        if (developerId) queryParams.append('developerId', developerId);

        fetch(`${url}?${queryParams.toString()}`)
        .then(response => {
            if(response.status === 200) {
                return response.json();
            }else {
                return Promise.reject(`Unexpected Status Code ${response.status}`)
            }
        })
        .then(data => setGames(data))
        .catch(console.log)
        .finally(() =>{
            setFetching(false);
        })
        
        
    }, [location.search]); // call me once on page load
    
    // handle delete
    const handleDeleteGame = (gameId) => {
        const game = games.find(g => g.gameId === gameId);
        console.log(gameId)
        if(window.confirm(`Delete Game: ${game.title}?`)) {
            const init = {
                method: 'DELETE'
            };
            fetch(`http://localhost:8080/v1/api/games/${gameId}`, init)
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
        <h2 className='mb-4'>Games</h2>
        {!fetching && (
                games.length > 0 ? (
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

            ) : (
                <p className="mt-4">Would you rather have unlimited bacon and no games? or games, unlimited games, and no games?</p>
            ))}
                <div>
                    <DeveloperFilter/>
                </div>
            
    </>);
}

export default GameList;