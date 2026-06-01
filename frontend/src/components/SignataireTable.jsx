export default function SignataireTable({ signataires, onSelect }) {
  return (
    <div className="card">
      <table className="table">
        <thead>
          <tr>
            <th style={{ width: 60 }}>État</th>
            <th>Civilité</th>
            <th>Signataire</th>
            <th>Fonction</th>
            <th>E-mail</th>
            <th style={{ width: 130 }}>Action</th>
          </tr>
        </thead>
        <tbody>
          {signataires.map((s) => {
            const signe = s.statut === "SIGNE";
            return (
              <tr
                key={s.id}
                className={signe ? "row signed" : "row pending"}
                onClick={() => onSelect(s)}
              >
                <td>
                  <span
                    className={"dot " + (signe ? "dot-green" : "dot-red")}
                    title={signe ? "Signé" : "En attente"}
                  />
                </td>
                <td>{s.civiliteLibelle}</td>
                <td className="name">{s.nomComplet}</td>
                <td>{s.fonction}</td>
                <td className="muted">{s.email}</td>
                <td>
                  {signe ? (
                    <span className="badge badge-green">Signé</span>
                  ) : (
                    <button className="btn-sign">Signer ▸</button>
                  )}
                </td>
              </tr>
            );
          })}
        </tbody>
      </table>
    </div>
  );
}
