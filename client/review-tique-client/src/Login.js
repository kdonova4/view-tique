import { useState } from "react";
import { useNavigate } from "react-router-dom";
import { useAuth } from "./context/AuthContext";

function Login() {
    const [username, setUsername] = useState();
    const [password, setPassword] = useState();
    const [error, setError] = useState("");
    const [loading, setLoading] = useState(false);
    const navigate = useNavigate();
    const { login } = useAuth();


    const handleSubmit = async (e) => {
        e.preventDefault();

        setLoading(true);
        setError("");

        const payload = { username, password }

        try {
            const response = await fetch(
                "http://localhost:8080/v1/api/user/authenticate",
                {
                    method: 'POST',
                    headers: {
                        "Content-Type": "application/json"
                    },
                    body: JSON.stringify(payload)
                }
            );

            if(response.ok) {
                const data = await response.json();
                console.log(data);
                login(data.jwt_token);
                navigate(-1);
            }else {
                const errorMessage = await response.text();
                setError (
                    errorMessage || "Login Failed, Please Try Again"
                )
            }
        } catch(err) {
            setError("Login Failed. Please Try Again");
            console.error("Login Error:", err);
        } finally {
            setLoading(false);
        }
    }

    return (
        <div className="container d-flex justify-content-center align-items-center min-vh-100">
          <div className="col-12 col-md-6 col-lg-4">
            <h2 className="text-center mb-4">Login</h2>
            <form onSubmit={handleSubmit}>
              <div className="mb-3">
                <label htmlFor="username" className="form-label">
                  Username
                </label>
                <input
                  type="text"
                  id="username"
                  className="form-control"
                  value={username}
                  onChange={(e) => setUsername(e.target.value)}
                  required
                />
              </div>
    
              <div className="mb-3">
                <label htmlFor="password" className="form-label">
                  Password
                </label>
                <input
                  type="password"
                  id="password"
                  className="form-control"
                  value={password}
                  onChange={(e) => setPassword(e.target.value)}
                  required
                />
              </div>
    
              {error && <div className="alert alert-danger">{error}</div>}
    
    
              <button
                type="submit"
                className="btn btn-primary w-100"
                disabled={loading}
              >
                {loading ? "Logging in..." : "Login"}
              </button>
            </form>
          </div>
        </div>
      );
}

export default Login;