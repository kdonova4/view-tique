import GameList from "./GameList";
import { FaRegCopyright } from "react-icons/fa";
function Home(){
    return(<>
        <div className="container">
            <header>
                <h1>Games</h1>
            </header>
            
            <footer>
                <FaRegCopyright/> COPYRIGHT 2025
            </footer>
        </div>
        
    </>)
}

export default Home;