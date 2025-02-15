import { useEffect, useState } from "react";
import { useNavigate, useParams, Link } from "react-router-dom";

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
    },
    genres: [],
    platforms: []
}

function GameForm(){
    // STATE
    const [game, setGame] = useState(GAME_DEFAULT);
    const [errors, setErrors] = useState([]);
    const url = 'http://localhost:8080/v1/api/games'

    const navigate = useNavigate();
    const { id } = useParams();

    // useEffect

    useEffect(() => {
        if(id){ //updating - populate the form with the game being updated
            fetch(`${url}/${id}`)
            .then(response => {
                if(response.status === 200){
                    return response.json();
                }else{
                    return Promise.reject(`Unexpected Status Code ${response.status}`);
                }
            })
            .then(data => {
                setGame(data)
            })
            .catch(console.log)
        }else{ // if no id in the form , means that were adding to reset the form to default;
            setGame(GAME_DEFAULT);
        }
    }, [id]); // hey react, please call my useEfect fundtion everytime the id in the url changes

    // Methods

    // handleSubmit
    const handleSubmit = (event) => {
        event.preventDefault();
        
        if(id) { // checks to see if there is an id in the url if there is update, else add
            updateGame();
        } else {
            addGame();
        }
    }
    // handleChange
    const handleChange = (event) => {
        const newGame = {...game}

        if(event.target.name === 'developer') {
            newGame.developer = {
                ...newGame.developer,
                developerName: event.target.value
            };
        } else{
            newGame[event.target.name] = event.target.value;
            
        }

        setGame(newGame);
    }
    // addGame
    const addGame = () => {
        
        const init = {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json'
            },
            body: JSON.stringify(game)
        }
        fetch(url, init)
        .then(response =>{
            if(response.status === 201 || response.status === 400){
                return response.json();
            } else{
                return Promise.reject(`Unexpected Status Code: ${response.status}`)
            }
        })
        .then(data =>{
            if(data.gameId) { //happy path
                navigate('/')
            } else {
                setErrors(data);
            }
        })
        .catch(console.log)
    }
    
    
    // updateGame

    const updateGame = () => {
        game.gameId = id;
        const init = {
            method: 'PUT',
            headers: {
                'Content-Type': 'application/json'
            },
            body: JSON.stringify(game)
        }
        fetch(`${url}/${id}`, init)
        .then(response =>{
            if(response.status === 204){
                return null;
            } else if(response.status === 400) {
                return response.json();
            }else{
                return Promise.reject(`Unexpected Status Code ${response.status}`);
            }
        })
        .then(data =>{
            if(!data){ //happy path
                navigate('/');
            } else { //unhappy
                //get our error messages and display them
                setErrors(data)
            }
        })
        .catch(console.log)
    }

    return(<>
        <section>
            <h2 className='mb-4'>{id > 0 ? 'Update Game' : 'Add Game'}</h2>
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
                    <button type='submit' className='btn btn-outline-success mr-4'>{id > 0 ? 'Update Game' : 'Add Game'}</button>
                    <Link type='button' className='btn btn-outline-danger mr-4' to={'/games'}>Cancel</Link>
                </fieldset>
            </form>
        </section>
    </>);
}

export default GameForm;