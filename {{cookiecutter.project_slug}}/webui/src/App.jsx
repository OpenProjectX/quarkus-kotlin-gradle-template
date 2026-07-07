import { useEffect, useState } from 'react'

function Greeting() {
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
    <section>
      <h2>Extension greeting</h2>
      <div className="row">
        <input value={name} onChange={(e) => setName(e.target.value)} placeholder="your name" />
        <button onClick={fetchGreeting} disabled={loading}>
          {loading ? 'Loading…' : 'Greet me'}
        </button>
      </div>
      {greeting && <p className="greeting">{greeting}</p>}
    </section>
  )
}

function Todos() {
  const [todos, setTodos] = useState([])
  const [title, setTitle] = useState('')

  const load = async () => {
    const res = await fetch('/todos')
    setTodos(await res.json())
  }

  useEffect(() => {
    load()
  }, [])

  const add = async () => {
    if (!title.trim()) return
    await fetch('/todos', {
      method: 'POST',
      headers: { 'Content-Type': 'application/json' },
      body: JSON.stringify({ title, completed: false, priority: 0 }),
    })
    setTitle('')
    load()
  }

  const toggle = async (t) => {
    await fetch(`/todos/${t.id}`, {
      method: 'PUT',
      headers: { 'Content-Type': 'application/json' },
      body: JSON.stringify({ ...t, completed: !t.completed }),
    })
    load()
  }

  const remove = async (t) => {
    await fetch(`/todos/${t.id}`, { method: 'DELETE' })
    load()
  }

  return (
    <section>
      <h2>Reactive CRUD (Todos)</h2>
      <div className="row">
        <input value={title} onChange={(e) => setTitle(e.target.value)} placeholder="new todo" />
        <button onClick={add}>Add</button>
      </div>
      <ul className="todos">
        {todos.map((t) => (
          <li key={t.id} className={t.completed ? 'done' : ''}>
            <input type="checkbox" checked={t.completed} onChange={() => toggle(t)} />
            <span>{t.title}</span>
            <button className="link" onClick={() => remove(t)}>delete</button>
          </li>
        ))}
      </ul>
    </section>
  )
}

export default function App() {
  return (
    <main className="container">
      <h1>{{ cookiecutter.project_name }}</h1>
      <p>
        React UI (Vite) served by Quarkus via <strong>Quinoa</strong>. It calls the
        extension&apos;s <code>/greeting</code> endpoint and the reactive
        <code>/todos</code> CRUD API (Panache Reactive + Liquibase + Dev Services).
      </p>
      <Greeting />
      <Todos />
    </main>
  )
}
