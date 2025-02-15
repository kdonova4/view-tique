import { BrowserRouter as Router } from "react-router-dom";
import { Route, Routes } from "react-router-dom";
import GameForm from "./GameForm";
import GameList from "./GameList";
import Home from "./Home";
import NotFound from "./NotFound";
import NavBar from "./NavBar";
import ReviewList from "./ReviewList";
import GamePage from "./GamePage";

function App() {
  return (
    <Router>
      <NavBar/>
      <Routes>
        <Route path="/" element={<Home/>}/>
        <Route path="/game/add" element={<GameForm/>}/>
        <Route path="/reviews/:gameId" element={<ReviewList/>}/>
        <Route path="/games/:gameId" element={<GamePage/>}/>
        <Route path="/game/edit/:id" element={<GameForm/>}/>
        <Route path="/games" element={<GameList/>}/>
        <Route path="*" element={<NotFound/>}/>
      </Routes>
    </Router>
  );
}

export default App;
