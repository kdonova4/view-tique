import { useEffect, useState } from 'react'
// const GAMES_DATA = [
//     {
//         gameId: 1,
//         title: "Deus Ex: Mankind Divided",
//         description: "Test",
//         releaseDate: "2016-08-23",
//         avgUserScore: 7.8,
//         avgCriticScore: 8.4,
//         userReviewCount: 12,
//         criticReviewCount: 6,
//         developer:
//         {
//             developerId: 5,
//             developerName: "Eidos Montreal"
//         }
//     },
//     {
//         gameId: 2,
//         title: "Halo 2",
//         description: "Test",
//         releaseDate: "2004-11-09",
//         avgUserScore: 9.4,
//         avgCriticScore: 8.9,
//         userReviewCount: 34,
//         criticReviewCount: 17,
//         developer:
//         {
//             developerId: 1,
//             developerName: "Bungie"
//         }
//     },
//     {
//         gameId: 3,
//         title: "Witcher 3",
//         description: "Test",
//         releaseDate: "2015-05-18",
//         avgUserScore: 9.5,
//         avgCriticScore: 9.1,
//         userReviewCount: 78,
//         criticReviewCount: 56,
//         developer:
//         {
//             developerId: 4,
//             developerName: "CD Projekt Red"
//         }
//     }
// ];

const GAME_DEFAULT = {
    title: "",
    description: "",
    releaseDate: "",
    avgUserScore: 0.0,
    avgCriticScore: 0.0,
    userReviewCount: 0,
    criticReviewCount: 0,
    developer: 
    {
        developerId: 4,
        developerName: "CD Projekt Red"
    }
}


function Games() {
    const[games, setGames] = useState([]);
    const [game, setGame] = useState(GAME_DEFAULT);
    const[currentView, setCurrentView] = useState('List');
    const [editGameId, setEditGameId] = useState(0);
    const [errors, setErrors] = useState([]);
    const url = 'http://localhost:8080/v1/api/games'

    // use Effect
    useEffect(() =>{
        fetch(url)
        .then(response => {
            if(response.status === 200){
                return response.json()
            }else{
                return Promise.reject(`Unexpected Status Code ${response.status}`)
            }
        })
        .then(data => setGames(data))
        .catch(console.log)
    }, []); // this empty array tells use effect to render once on page load

    // CRUD
    const handleEditGame = (gameId) => {
        console.log("Editing Game")
    }

    
    const handleDeleteGame = (gameId) => {
        console.log("Deleting Game")
    }
    
    const handleSubmit = (event) => {
        event.preventDefault();
        if(editGameId === 0) {
            // add game
        } else {
            // update game
        }
    }

    const handleChange = (event) => {
        const newGame = {... game}

        newGame[event.target.name] = event.target.value;
        console.log(newGame[event.target.name]);
        setGame(newGame);
        console.log(game);

    }

    // HELPER FUNCTION
    const resetState = () => {
        console.log(game)
        setGame(GAME_DEFAULT);
        setCurrentView('List');
        setEditGameId(0);
        setErrors([]);
    }


    return(<>
        {(currentView === 'List') && (
            <>
            <section>
                <h2 className='mb-4'>Games</h2>
                <button className='btn btn-outline-success' onClick={() => setCurrentView('Add') }>Add a Game</button>
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
                                    <button className='btn btn-outline-warning mr-4' onClick={() => handleEditGame(game.gameId)}>Update</button>
                                    <button className='btn btn-outline-danger mr-4' onClick={() => handleDeleteGame(game.gameId)}>Delete</button>
                                </td>
                            </tr>
                        ))}
                    </tbody>
                </table>
            </section>
            </>
        )}

        {(currentView === 'Add' || currentView === 'Edit') && (
            <>
                <section>
                    <h2 className='mb-4'>{editGameId > 0 ? 'Update Game' : 'Add Game'}</h2>
                    {errors.length > 0 && (
                        <div className='alert alert-danger'>
                            <p>The following errors were found: </p>
                            <ul>
                                {errors.map(error => (
                                    <li key={error}>{error}</li>
                                ))}
                            </ul>
                        </div>
                    )}
                    <form onSubmit={handleSubmit}>
                        <fieldset className='form-group'>
                            <label htmlFor='title'>Title</label>
                            <input
                            id='title'
                            name='title'
                            type='text'
                            className='formControl'
                            value={game.title}
                            onChange={handleChange}
                            />
                        </fieldset>
                        <fieldset className='form-group'>
                            <label htmlFor='description'>Description</label>
                            <input
                            id='description'
                            name='description'
                            type='text'
                            className='formControl'
                            value={game.description}
                            onChange={handleChange}
                            />
                        </fieldset>
                        <fieldset className='form-group'>
                            <label htmlFor='releaseDate'>Release Date</label>
                            <input
                            id='releaseDate'
                            name='releaseDate'
                            type='date'
                            className='formControl'
                            value={game.releaseDate}
                            onChange={handleChange}
                            />
                        </fieldset>
                        <fieldset className='form-group'>
                            <label htmlFor='avgUserScore'>Avg User Score</label>
                            <input
                            id='avgUserScore'
                            name='avgUserScore'
                            type='number'
                            step='0.1'
                            className='formControl'
                            value={game.avgUserScore}
                            onChange={handleChange}
                            />
                        </fieldset>
                        <fieldset className='form-group'>
                            <label htmlFor='avgCriticScore'>Avg Critic Score</label>
                            <input
                            id='avgCriticScore'
                            name='avgCriticScore'
                            type='number'
                            step='0.1'
                            className='formControl'
                            value={game.avgCriticScore}
                            onChange={handleChange}
                            />
                        </fieldset>
                        <fieldset className='form-group'>
                            <label htmlFor='userReviewCount'>User Review Count</label>
                            <input
                            id='userReviewCount'
                            name='userReviewCount'
                            type='number'
                            className='formControl'
                            value={game.userReviewCount}
                            onChange={handleChange}
                            />
                        </fieldset>
                        <fieldset className='form-group'>
                            <label htmlFor='criticReviewCount'>Critic Review Count</label>
                            <input
                            id='criticReviewCount'
                            name='criticReviewCount'
                            type='number'
                            className='formControl'
                            value={game.criticReviewCount}
                            onChange={handleChange}
                            />
                        </fieldset>
                        <fieldset className='form-group'>
                            <label htmlFor='developer'>Developer</label>
                            <input
                            id='developer'
                            name='developer'
                            type='text'
                            className='formControl'
                            value={game.developer.developerName}
                            onChange={handleChange}
                            />
                        </fieldset>
                        <fieldset className='form-group'>
                            <button type='submit' className='btn btn-outline-success mr-4'>{editGameId > 0 ? 'Update Game' : 'Add Game'}</button>
                            <button type='button' className='btn btn-outline-danger mr-4' onClick={() => resetState()}>Cancel</button>
                        </fieldset>
                    </form>
                </section>
            </>
        )}
    </>)
}

export default Games;  