import { useState } from 'react'

export default function App() {
  const [name, setName] = useState('world')
  const [greeting, setGreeting] = useState('')
  const [loading, setLoading] = useState(false)

  const fetchGreeting = async () => {
    setLoading(true)
    try {
      // Calls the Quarkus REST endpoint contributed by the extension.
      const res = await fetch(`/greeting/${encodeURIComponent(name)}`)
      setGreeting(await res.text())
    } catch (e) {
      setGreeting('Error: ' + e.message)
    } finally {
      setLoading(false)
    }
  }

  return (
    <main className="container">
      <h1>{{ cookiecutter.project_name }}</h1>
      <p>
        React UI (Vite) served by Quarkus via <strong>Quinoa</strong>, calling the
        extension&apos;s <code>/greeting</code> endpoint.
      </p>
      <div className="row">
        <input
          value={name}
          onChange={(e) => setName(e.target.value)}
          placeholder="your name"
        />
        <button onClick={fetchGreeting} disabled={loading}>
          {loading ? 'Loading…' : 'Greet me'}
        </button>
      </div>
      {greeting && <p className="greeting">{greeting}</p>}
    </main>
  )
}
