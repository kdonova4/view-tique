import { useEffect, useState } from "react";
import { Link, useLocation } from "react-router-dom";
import DeveloperFilter from "./DeveloperFilter";
import PlatformFilter from "./PlatformFilter";
import GenreFilter from "./GenreFilter";
import { Container, Modal } from "react-bootstrap";
import ImageComponent from "./ImageComponent";

function GameList() {
    // STATE
    const [games, setGames] = useState([]);
    const [gameName, setGameName] = useState('')
    const [genres, setGenres] = useState([]);
    const [platforms, setPlatforms] = useState([]);
    const [developerId, setDeveloperId] = useState('');
    const [fetching, setFetching] = useState(true);

    const url = 'http://3.13.229.3:8080/api/v1/games/search'
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

        if (gameName) queryParams.append('gameName', gameName);
        if (genres.length > 0) queryParams.append('genres', genres.join(','));
        if (platforms.length > 0) queryParams.append('platforms', platforms.join(','));
        if (developerId) queryParams.append('developerId', developerId);

        fetch(`${url}?${queryParams.toString()}`)
            .then(response => {
                if (response.status === 200) {
                    return response.json();
                } else {
                    return Promise.reject(`Unexpected Status Code ${response.status}`)
                }
            })
            .then(data => setGames(data))
            .catch(console.log)
            .finally(() => {
                setFetching(false);
                console.log(games)
            })


    }, [location.search]); // call me once on page load

    // handle delete
    const handleDeleteGame = (gameId) => {
        const game = games.find(g => g.gameId === gameId);
        console.log(gameId)
        if (window.confirm(`Delete Game: ${game.title}?`)) {
            const init = {
                method: 'DELETE'
            };
            fetch(`http://3.13.229.3:8080/api/v1/games/${gameId}`, init)
                .then(response => {
                    if (response.status === 204) {
                        // create a copy of the array remove the game
                        const newGames = games.filter(g => g.gameId !== gameId)
                        // update the game state
                        setGames(newGames);
                    } else {
                        return Promise.reject(`Unexpected Status Code ${response.status}`)
                    }
                })
                .catch(console.log);
        }
    }

    return (<>

<section className="container">
    <h2 className='mb-4'>Search Results</h2>
    <div className="results-filter">
        <div className="game-container">
            {!fetching ? (
                games.length > 0 ? (
                    games.map(game => (
                        <Link to={`/games/${game.gameId}`} key={game.id}>
                            <div className="results">

                                <div className="cover">
                                    <ImageComponent src={game.cover.replace("t_cover_big", "t_logo_med")} />
                                </div>

                                <div className="info">
                                    <div className="media">
                                        <span to={`/games/${game.gameId}`}>
                                            {game.title}
                                            <time className="ml-2 text-muted small" dateTime={game.releaseDate}>
                                                {new Date(game.releaseDate).getFullYear()}
                                            </time>
                                        </span>

                                        <span className="text-muted">
                                            {game.developer.developerName}
                                        </span>
                                    </div>
                                </div>
                            </div>
                        </Link>
                    ))
                ) : (
                    <p className="mt-4">
                        Would you rather have unlimited bacon and no games? or games, unlimited games, and no games?
                    </p>
                )
            ) : null}
        </div>
        <div className="filter-container">
            <h6 className="title">&nbsp; Filters</h6>
            <DeveloperFilter />
            <PlatformFilter />
            <GenreFilter />
        </div>
    </div>
</section>
    </>);
}

export default GameList;