import React from 'react'
import ReactDOM from 'react-dom/client'
import { CssBaseline, Container } from '@mui/material'
import { createTheme, ThemeProvider } from '@mui/material/styles'
import App from './modules/App'

const theme = createTheme({
  palette: {
    mode: 'light',
    primary: { main: '#7C4DFF' }, // deep purple accent
    secondary: { main: '#00E5FF' }, // cyan accent
    background: { default: '#f7f7fb' }
  },
  shape: { borderRadius: 10 },
  typography: { fontFamily: 'Inter, Roboto, Helvetica, Arial, sans-serif' }
})

ReactDOM.createRoot(document.getElementById('root')!).render(
  <React.StrictMode>
    <ThemeProvider theme={theme}>
      <CssBaseline />
      <Container maxWidth="md" sx={{ py: 4 }}>
        <App />
      </Container>
    </ThemeProvider>
  </React.StrictMode>
)
