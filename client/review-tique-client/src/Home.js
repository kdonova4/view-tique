import GameList from "./GameList";

function Home(){
    return(<>
        <div className="container">
            <header>
                <h1>Games</h1>
            </header>
            <section className="about">
                <h2>About Review-Tique</h2>
                <p>Godfather ipsum dolor sit amet. Why do you hurt me, Michael? I've always been loyal to you. We're both part of the same hypocrisy, senator, but never think it applies to my family. You can act like a man! Mr Corleone is Johnny Fontane's godfather. Now Italians regard that as a very close, a very sacred religious relationship.</p>
            </section>

            <section>
                <GameList/>
            </section>
            <footer>
                COPYRIGHT 2025
            </footer>
        </div>
        
    </>)
}

export default Home;