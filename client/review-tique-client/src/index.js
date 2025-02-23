import React from 'react';
import ReactDOM from 'react-dom/client';
import './index.css';
import App from './App';
import { AuthProvider } from './context/AuthContext';
import { WishlistProvider } from './context/WishlistContext';


const root = ReactDOM.createRoot(document.getElementById('root'));
root.render(
  
  <AuthProvider>
    <WishlistProvider>
    <App/>
    </WishlistProvider>  
  </AuthProvider>
);


