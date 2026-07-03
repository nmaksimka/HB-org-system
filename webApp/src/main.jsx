import React, { useEffect, useMemo, useState } from "react";
import { createRoot } from "react-dom/client";
import {
  Bell, CalendarDays, Cake, ChevronRight, Gift, LogOut, Menu,
  Plus, Search, Sparkles, Users, UserRound, X
} from "lucide-react";
import "./styles.css";

const API = import.meta.env.VITE_API_URL || "";

async function request(path, options = {}) {
  const token = localStorage.getItem("birthday.token");
  const response = await fetch(`${API}${path}`, {
    ...options,
    headers: {
      ...(options.body && !(options.body instanceof FormData) ? { "Content-Type": "application/json" } : {}),
      ...(token ? { Authorization: `Bearer ${token}` } : {}),
      ...(options.headers || {})
    }
  });
  if (!response.ok) {
    const error = await response.json().catch(() => ({}));
    throw new Error(error.message || `Ошибка запроса (${response.status})`);
  }
  return response.status === 204 ? null : response.json();
}

const initials = (user) =>
  `${user?.firstName?.[0] || user?.username?.[0] || "?"}${user?.lastName?.[0] || ""}`.toUpperCase();

function Auth({ onAuthenticated }) {
  const [mode, setMode] = useState("login");
  const [error, setError] = useState("");
  const [busy, setBusy] = useState(false);

  async function submit(event) {
    event.preventDefault();
    setBusy(true); setError("");
    const data = Object.fromEntries(new FormData(event.currentTarget));
    try {
      const result = await request(`/api/v1/auth/${mode}`, {
        method: "POST",
        body: JSON.stringify(data)
      });
      localStorage.setItem("birthday.token", result.accessToken);
      onAuthenticated(result.user);
    } catch (e) { setError(e.message); } finally { setBusy(false); }
  }

  return <main className="auth-page">
    <section className="auth-story">
      <div className="brand"><span className="brand-mark"><Cake /></span> Birthday</div>
      <div>
        <span className="eyebrow">Праздники становятся ближе</span>
        <h1>Помните важное.<br/><em>Дарите желанное.</em></h1>
        <p>Дни рождения друзей, вишлисты, тайные обсуждения подарков и общие сборы — в одном уютном месте.</p>
      </div>
      <div className="story-card">
        <div className="mini-avatars"><span>М</span><span>А</span><span>К</span></div>
        <div><strong>Ни один праздник не потеряется</strong><small>Напомним заранее и поможем подготовиться вместе</small></div>
      </div>
    </section>
    <section className="auth-panel">
      <div className="mobile-brand brand"><span className="brand-mark"><Cake /></span> Birthday</div>
      <form className="auth-card" onSubmit={submit}>
        <span className="eyebrow">{mode === "login" ? "С возвращением" : "Добро пожаловать"}</span>
        <h2>{mode === "login" ? "Войти в аккаунт" : "Создать аккаунт"}</h2>
        <p>{mode === "login" ? "Продолжим готовить хорошие сюрпризы." : "Пара минут — и важные даты под присмотром."}</p>
        {mode === "register" && <div className="form-grid">
          <label>Имя<input name="firstName" required placeholder="Максим" /></label>
          <label>Фамилия<input name="lastName" placeholder="Никонов" /></label>
          <label className="wide">Имя пользователя<input name="username" required minLength="3" placeholder="maksim" /></label>
          <label className="wide">Дата рождения<input name="birthDate" required type="date" /></label>
        </div>}
        <label>Email<input name="email" required type="email" placeholder="you@example.com" /></label>
        <label>Пароль<input name="password" required type="password" minLength="8" placeholder="Не менее 8 символов" /></label>
        {error && <div className="error">{error}</div>}
        <button className="primary" disabled={busy}>{busy ? "Подождите…" : mode === "login" ? "Войти" : "Зарегистрироваться"} <ChevronRight /></button>
        <button type="button" className="text-button" onClick={() => { setMode(mode === "login" ? "register" : "login"); setError(""); }}>
          {mode === "login" ? "Нет аккаунта? Создать" : "Уже есть аккаунт? Войти"}
        </button>
      </form>
    </section>
  </main>;
}

const nav = [
  ["home", Sparkles, "Главная"], ["people", UserRound, "Люди"],
  ["groups", Users, "Группы"], ["gifts", Gift, "Вишлист"],
  ["calendar", CalendarDays, "Календарь"], ["notifications", Bell, "Уведомления"]
];

function Shell({ user, onLogout }) {
  const [page, setPage] = useState("home");
  const [mobile, setMobile] = useState(false);
  const Current = { home: Home, people: People, groups: Groups, gifts: Gifts, calendar: Calendar, notifications: Notifications }[page];
  return <div className="app">
    <aside className={mobile ? "sidebar open" : "sidebar"}>
      <div className="brand"><span className="brand-mark"><Cake /></span> Birthday <button className="close-nav" onClick={() => setMobile(false)}><X/></button></div>
      <nav>{nav.map(([id, Icon, title]) =>
        <button key={id} className={page === id ? "active" : ""} onClick={() => { setPage(id); setMobile(false); }}><Icon />{title}</button>)}</nav>
      <div className="sidebar-user"><span>{initials(user)}</span><div><strong>{user.firstName || user.username}</strong><small>{user.email}</small></div><button onClick={onLogout} title="Выйти"><LogOut/></button></div>
    </aside>
    <div className="content">
      <header><button className="menu" onClick={() => setMobile(true)}><Menu/></button><div className="header-brand">Birthday</div><button className="avatar">{initials(user)}</button></header>
      <Current user={user} go={setPage} />
    </div>
  </div>;
}

function Page({ eyebrow, title, action, children }) {
  return <main className="page"><div className="page-heading"><div><span className="eyebrow">{eyebrow}</span><h1>{title}</h1></div>{action}</div>{children}</main>;
}

function Home({ user, go }) {
  return <Page eyebrow="Ваш праздник-помощник" title={`Добрый день, ${user.firstName || user.username}!`}>
    <section className="hero-card"><div><span className="eyebrow light">Всё под контролем</span><h2>Подарки начинаются<br/>с внимания</h2><p>Найдите друзей, загляните в их вишлисты и подготовьте сюрприз без лишней суеты.</p><button className="light-button" onClick={() => go("people")}>Найти друзей <Search/></button></div><div className="hero-art"><Gift/><span>✦</span><span>✦</span></div></section>
    <section className="quick-grid">
      <button onClick={() => go("gifts")}><span className="quick-icon coral"><Gift/></span><div><strong>Мой вишлист</strong><small>Соберите идеи подарков</small></div><ChevronRight/></button>
      <button onClick={() => go("groups")}><span className="quick-icon blue"><Users/></span><div><strong>Группы друзей</strong><small>Готовьтесь к праздникам вместе</small></div><ChevronRight/></button>
      <button onClick={() => go("calendar")}><span className="quick-icon green"><CalendarDays/></span><div><strong>Календарь</strong><small>Добавьте важные даты</small></div><ChevronRight/></button>
    </section>
  </Page>;
}

function People() {
  const [query, setQuery] = useState(""); const [items, setItems] = useState([]); const [error, setError] = useState("");
  async function search(e) { e?.preventDefault(); try { const r = await request(`/api/v1/users?search=${encodeURIComponent(query)}&size=24`); setItems(r.content || []); } catch(e){ setError(e.message); } }
  useEffect(() => { search(); }, []);
  return <Page eyebrow="Сообщество" title="Найдите близких">
    <form className="search" onSubmit={search}><Search/><input value={query} onChange={e=>setQuery(e.target.value)} placeholder="Имя пользователя"/><button>Найти</button></form>
    {error && <div className="error">{error}</div>}<div className="cards">{items.map(u=><article className="person-card" key={u.id}><span className="large-avatar">{initials(u)}</span><strong>{u.firstName} {u.lastName}</strong><small>@{u.username}</small>{u.birthDate && <p><Cake/> {u.birthDate}</p>}</article>)}</div>
  </Page>;
}

function Groups() {
  const [items,setItems]=useState([]),[name,setName]=useState(""),[error,setError]=useState("");
  const load=()=>request("/api/v1/groups").then(r=>setItems(r.content||[])).catch(e=>setError(e.message));
  useEffect(load,[]);
  async function create(e){e.preventDefault();try{await request("/api/v1/groups",{method:"POST",body:JSON.stringify({name,description:"",publicGroup:false})});setName("");load();}catch(e){setError(e.message);}}
  return <Page eyebrow="Вместе веселее" title="Ваши группы" action={<button className="round"><Plus/></button>}>
    <form className="inline-form" onSubmit={create}><input required value={name} onChange={e=>setName(e.target.value)} placeholder="Название новой группы"/><button className="primary">Создать</button></form>
    {error&&<div className="error">{error}</div>}<div className="cards">{items.map(g=><article className="group-card" key={g.id}><span className="quick-icon blue"><Users/></span><strong>{g.name}</strong><p>{g.description||"Закрытая группа для подготовки сюрпризов"}</p><small>Ваша роль: {g.currentUserRole}</small></article>)}</div>
  </Page>;
}

function Gifts({ user }) {
  const [items,setItems]=useState([]),[title,setTitle]=useState(""),[error,setError]=useState("");
  const load=()=>request(`/api/v1/users/${user.id}/gifts`).then(setItems).catch(e=>setError(e.message));
  useEffect(load,[user.id]);
  async function create(e){e.preventDefault();try{await request("/api/v1/gifts",{method:"POST",body:JSON.stringify({title,description:"",currency:"RUB",priority:"MEDIUM",visibility:"PUBLIC"})});setTitle("");load();}catch(e){setError(e.message);}}
  return <Page eyebrow="Мечты и идеи" title="Мой вишлист">
    <form className="inline-form" onSubmit={create}><input required value={title} onChange={e=>setTitle(e.target.value)} placeholder="Что вы хотели бы получить?"/><button className="primary"><Plus/> Добавить</button></form>
    {error&&<div className="error">{error}</div>}<div className="gift-list">{items.map(g=><article key={g.id}><span className="quick-icon coral"><Gift/></span><div><strong>{g.title}</strong><small>{g.description||"Без описания"}</small></div><span className={`badge ${g.status?.toLowerCase()}`}>{g.status}</span></article>)}</div>
  </Page>;
}

function Calendar() {
  const [integrations,setIntegrations]=useState([]),[token,setToken]=useState(""),[error,setError]=useState("");
  const load=()=>request("/api/v1/calendar/integrations").then(setIntegrations).catch(e=>setError(e.message));
  useEffect(load,[]);
  async function connect(e){e.preventDefault();try{await request("/api/v1/calendar/integrations",{method:"POST",body:JSON.stringify({provider:"GOOGLE",accessToken:token,calendarId:"primary"})});setToken("");load();}catch(e){setError(e.message);}}
  return <Page eyebrow="Важные даты" title="Календарь">
    <section className="empty-feature"><span className="quick-icon green"><CalendarDays/></span><div><h3>Подключите календарь</h3><p>События дней рождения будут всегда под рукой. Токен хранится только в зашифрованном виде.</p></div></section>
    <form className="inline-form" onSubmit={connect}><input required type="password" value={token} onChange={e=>setToken(e.target.value)} placeholder="Access token Google Calendar"/><button className="primary">Подключить</button></form>
    {error&&<div className="error">{error}</div>}<div className="gift-list">{integrations.map(i=><article key={i.id}><span className="quick-icon green"><CalendarDays/></span><div><strong>{i.provider}</strong><small>Календарь: {i.calendarId}</small></div><span className="badge active">{i.status}</span></article>)}</div>
  </Page>;
}

function Notifications() {
  const [items,setItems]=useState([]),[error,setError]=useState("");
  const load=()=>request("/api/v1/notifications").then(r=>setItems(r.content||r)).catch(e=>setError(e.message));
  useEffect(load,[]);
  async function read(id){await request(`/api/v1/notifications/${id}/read`,{method:"PATCH"});load();}
  return <Page eyebrow="Будьте в курсе" title="Уведомления">
    {error&&<div className="error">{error}</div>}<div className="notification-list">{items.length===0&&<div className="empty">Пока здесь тихо — новые события появятся здесь.</div>}{items.map(n=><button key={n.id} className={n.status==="UNREAD"?"unread":""} onClick={()=>read(n.id)}><span className="quick-icon coral"><Bell/></span><div><strong>{n.title}</strong><p>{n.message}</p></div></button>)}</div>
  </Page>;
}

function App() {
  const [user,setUser]=useState(null),[loading,setLoading]=useState(Boolean(localStorage.getItem("birthday.token")));
  useEffect(()=>{if(!loading)return;request("/api/v1/auth/me").then(setUser).catch(()=>localStorage.removeItem("birthday.token")).finally(()=>setLoading(false));},[]);
  if(loading)return <div className="splash"><span className="brand-mark"><Cake/></span><strong>Birthday</strong></div>;
  if(!user)return <Auth onAuthenticated={setUser}/>;
  return <Shell user={user} onLogout={()=>{localStorage.removeItem("birthday.token");setUser(null);}}/>;
}

createRoot(document.getElementById("root")).render(<App/>);
