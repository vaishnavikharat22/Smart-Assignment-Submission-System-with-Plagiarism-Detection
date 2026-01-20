# Frontend Setup Guide

## Prerequisites

- Node.js 16+ 
- npm 8+

## Installation

```bash
cd frontend

# Install dependencies
npm install

# Start development server
npm start
```

Frontend will run on `http://localhost:3000`

## Project Structure

```
src/
├── components/
│   ├── AssignmentCard.js      # Assignment display card
│   ├── SubmissionModal.js     # File upload modal
│   └── Navigation.js          # Top navigation bar
├── pages/
│   ├── Login.js               # Login page
│   ├── Register.js            # Registration page
│   ├── StudentDashboard.js    # Student main page
│   └── TeacherDashboard.js    # Teacher main page
├── services/
│   └── api.js                 # API client services
├── context/
│   └── AuthContext.js         # Authentication context
├── styles/
│   ├── index.css              # Global styles
│   ├── App.css                # App component styles
│   ├── Auth.css               # Auth pages styles
│   ├── Dashboard.css          # Dashboard styles
│   ├── Navigation.css         # Navigation styles
│   ├── AssignmentCard.css     # Card styles
│   └── Modal.css              # Modal styles
├── App.js                     # Main component
└── index.js                   # Entry point
```

## API Configuration

Update API base URL in `src/services/api.js`:

```javascript
const API_BASE_URL = 'http://localhost:8080/api';
```

## Available Scripts

### Development
```bash
npm start
```

Runs the app in development mode at http://localhost:3000

### Build for Production
```bash
npm run build
```

Creates optimized production build in `build/` folder

### Run Tests
```bash
npm test
```

Launches test runner in interactive mode

### Eject Configuration
```bash
npm run eject
```

⚠️ **One-way operation** - Exposes all build configuration

## Authentication Flow

1. **Login/Register**: User enters credentials
2. **Token Received**: JWT token stored in localStorage
3. **Protected Routes**: Token added to Authorization header
4. **Auto-logout**: Token cleared on logout or API error

## Component Hierarchy

```
App
├── Navigation
├── Routes
│   ├── Login
│   ├── Register
│   └── Dashboard
│       ├── StudentDashboard
│       │   ├── AssignmentCard
│       │   └── SubmissionModal
│       └── TeacherDashboard
│           └── AssignmentCard
```

## Styling

Uses modern CSS3 with:
- CSS Grid for layouts
- Flexbox for components
- CSS variables for colors
- Responsive design

### Color Scheme
- Primary: #667eea (Purple)
- Secondary: #3498db (Blue)
- Success: #27ae60 (Green)
- Error: #e74c3c (Red)
- Background: #f5f5f5 (Light Gray)

## Environment Variables

Create `.env` file in frontend directory:

```
REACT_APP_API_URL=http://localhost:8080/api
REACT_APP_JWT_TOKEN_NAME=token
```

## Dependencies

### Core
- **react**: UI framework
- **react-dom**: React renderer
- **react-router-dom**: Client-side routing

### HTTP
- **axios**: Promise-based HTTP client

### Utilities
- **classnames**: CSS class binding
- **jwtdecode**: JWT token decoding
- **date-fns**: Date formatting

## Common Issues

### Can't connect to backend
1. Verify backend is running on `http://localhost:8080`
2. Check CORS settings in backend
3. Open browser DevTools → Network tab to see API calls

### Blank page after login
1. Check browser console for errors (F12)
2. Verify token is saved in localStorage
3. Check network requests in Network tab

### Styles not loading
1. Clear browser cache (Ctrl+Shift+Delete)
2. Stop dev server and restart: `npm start`
3. Check CSS file imports in components

### npm install fails
```bash
# Clear npm cache
npm cache clean --force

# Remove node_modules and package-lock.json
rm -rf node_modules package-lock.json

# Reinstall
npm install
```

## Performance Optimization

- Code splitting with React.lazy()
- Component memoization
- Image optimization
- CSS minification

## Deployment

### Build for Production
```bash
npm run build
```

### Deploy to Netlify
```bash
npm install -g netlify-cli
netlify deploy --prod --dir=build
```

### Deploy to Vercel
```bash
npm install -g vercel
vercel --prod
```

### Deploy to GitHub Pages
```bash
npm install --save-dev gh-pages
```

Add to package.json:
```json
"homepage": "https://yourusername.github.io/repo",
"predeploy": "npm run build",
"deploy": "gh-pages -d build"
```

Then: `npm run deploy`

## Browser Support

- Chrome (latest)
- Firefox (latest)
- Safari (latest)
- Edge (latest)

## Development Tools

Recommended VS Code extensions:
- ES7+ React/Redux/React-Native snippets
- Prettier
- ESLint
- Thunder Client (API testing)
