import { BrowserRouter as Router } from "react-router-dom";
import { Route, Routes } from "react-router-dom";
import GameForm from "./GameForm";
import GameList from "./GameList";
import Home from "./Home";
import NotFound from "./NotFound";
import Register from "./Register";
import NavBar from "./NavBar";
import ReviewList from "./ReviewList";
import GamePage from "./GamePage";
import GameSearch from "./GameSearch";
import { jwtDecode } from "jwt-decode";
import DeveloperFilter from "./DeveloperFilter";
import ProtectedRoute from "./ProtectedRoute";
import Login from "./Login";
import { Modal } from "react-bootstrap";
import UserProfile from "./UserProfile";
import TopGames from "./TopGames";

function App() {
  const token = localStorage.getItem("token");

  let role = null;
  let isAuthenticated = false;

  if(token) {
    try {
      const decodedToken = jwtDecode(token);
      role = decodedToken.authorities;
      isAuthenticated = true;
    } catch (e) {
      console.error("Invalid Token", e);
    }
  }

  return (
    <Router>
      <NavBar/>
      <Routes>
        <Route path="/" element={<Home/>}/>
        <Route path="/game/add" element={<GameForm/>}/>
        <Route path="/reviews/:gameId" element={<ReviewList/>}/>
        <Route path="/games/:gameId" element={<GamePage/>}/>
        <Route path="/game/edit/:id" element={<GameForm/>}/>
        <Route path="/games/search" element={<GameList/>}/>
        <Route path="/games/:genreId/:limit" element={<TopGames/>}/>
        <Route path="/search" element={<GameSearch/>}/>
        <Route path="*" element={<NotFound/>}/>
        <Route path="/developer" element={<DeveloperFilter/>}/>
        <Route path="/login"
        element={
          <ProtectedRoute requireAuth={false} redirectTo="/">
            <Login/>
          </ProtectedRoute>
        }
        />
        <Route
              path="/register"
              element={
                <ProtectedRoute requireAuth={false} redirectTo="/">
                  <Register />
                </ProtectedRoute>
              }
        />
        <Route
              path="/profile"
              element={
                <ProtectedRoute requireAuth={true} redirectTo="/login">
                  <UserProfile />
                </ProtectedRoute>
              }
        />
      </Routes>
    </Router>
  );
}

export default App;
