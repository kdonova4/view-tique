import { createContext, useContext, useEffect, useState } from "react";
import { useAuth } from "./AuthContext";
import { jwtDecode } from "jwt-decode";

const WishlistContext = createContext();

export function WishlistProvider({ children }) {
    const [wishlist, setWishlist] = useState([]);
    const { token } = useAuth();
    const url = 'http://3.13.229.3:8080/api/v1/wishlists'
    
    useEffect(() => {
        
        if(token) {
            fetch(`${url}/wishlist`, {
                headers: {
                    Authorization: `Bearer ${token}`,
                    "Content-Type": 'application/json'
                }
            })
            .then(response => {
                if(response.status === 200) {
                    return response.json()
                } else {
                    return Promise.reject(`Unexpected Status Code ${response.status}`)
                }
            })
            .then((data) => {
                setWishlist(data);
            })
            .catch(console.log)
        }
    }, [token])

    const addToWishlist = (gameId) => {
        const decodedToken = token ? jwtDecode(token) : null;
        const init = {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json',
                'Authorization': `Bearer ${token}`
            },
            body: JSON.stringify({
                userId: decodedToken.appUserId,
                gameId: gameId
            })
        }
        fetch(url, init)
        .then(response => {
            if(response.status === 201) {
                return response.json()
            } else {
                return Promise.reject(`Unexpected Status Code ${response.status}`)
            }
        })
        .then(data => {

            setWishlist((prev) => [...prev, data]);
        })
        .catch(console.log)
    }

    const removeFromWishlist = (gameId) => {
        if(window.confirm(`Remove Game From Wishlist?`)) {
            const headers = {
                'Authorization': `Bearer ${token}`,
                'Content-Type': 'application/json'
            }
            const init = {
                method: 'DELETE',
                headers: headers
            }

            const wishlistedItem = wishlist.find((item) => item.gameId === Number(gameId))

            if(!wishlistedItem) {
                console.error("Game Not Found")
                return;
            }
            fetch(`${url}/${wishlistedItem.wishlistId}`, init)
                .then(response => {
                    if(response.status === 204) {
                        const newWishlist = wishlist.filter(wishlistGame => wishlistGame.wishlistId !== wishlistedItem.wishlistId);
                        setWishlist(newWishlist);
                        console.log(newWishlist)
                    } else {
                        return Promise.reject(`Unexpected Status Code ${response.status}`)
                    }
                })
                .catch(console.log)
        }
    }



    return (
        <WishlistContext.Provider value={{ wishlist, addToWishlist, removeFromWishlist }}>
            {children}
        </WishlistContext.Provider>
    )

}

export function useWishlist() {
    return useContext(WishlistContext);
}