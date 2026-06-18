package com.volunteer.userservice.adapters.in.rest;

import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;

@Path("/")
public class RootResource {

    @GET
    @Produces(MediaType.TEXT_HTML)
    public String home() {
        return """
                <!DOCTYPE html>
                <html lang="en">
                <head>
                    <meta charset="UTF-8">
                    <meta name="viewport" content="width=device-width, initial-scale=1.0">
                    <title>User Service</title>
                    <style>
                        * { box-sizing: border-box; }
                        body {
                            margin: 0;
                            min-height: 100vh;
                            font-family: Arial, Helvetica, sans-serif;
                            background: #0f172a;
                            color: #f8fafc;
                            display: grid;
                            place-items: center;
                            padding: 32px;
                        }
                        main { width: min(960px, 100%); }
                        .top {
                            display: flex;
                            justify-content: space-between;
                            align-items: flex-start;
                            gap: 24px;
                            flex-wrap: wrap;
                            padding-bottom: 28px;
                            border-bottom: 1px solid #334155;
                        }
                        .eyebrow {
                            color: #86efac;
                            font-size: 13px;
                            font-weight: 700;
                            text-transform: uppercase;
                        }
                        h1 { margin: 10px 0 0; font-size: 48px; line-height: 1.05; }
                        h2 { margin: 10px 0 0; color: #cbd5e1; font-size: 22px; font-weight: 500; }
                        .badge {
                            display: inline-flex;
                            align-items: center;
                            gap: 9px;
                            padding: 9px 13px;
                            border: 1px solid #4ade80;
                            border-radius: 999px;
                            color: #bbf7d0;
                            background: #14532d;
                            font-size: 13px;
                            font-weight: 700;
                        }
                        .dot { width: 9px; height: 9px; border-radius: 50%; background: #4ade80; }
                        .description {
                            max-width: 760px;
                            margin: 28px 0;
                            color: #dbeafe;
                            font-size: 17px;
                            line-height: 1.7;
                        }
                        .cards {
                            display: grid;
                            grid-template-columns: repeat(auto-fit, minmax(180px, 1fr));
                            gap: 14px;
                        }
                        .card {
                            min-height: 126px;
                            padding: 20px;
                            border: 1px solid #334155;
                            border-radius: 8px;
                            background: #1e293b;
                            color: #f8fafc;
                            text-decoration: none;
                            transition: transform 160ms ease, border-color 160ms ease;
                        }
                        .card:hover { transform: translateY(-3px); border-color: #4ade80; }
                        .card strong { display: block; margin-bottom: 9px; font-size: 18px; }
                        .card span { color: #cbd5e1; font-size: 14px; line-height: 1.5; }
                        .info { margin-top: 30px; padding-top: 24px; border-top: 1px solid #334155; }
                        .info h3 { margin: 0 0 14px; font-size: 18px; }
                        .info-grid {
                            display: grid;
                            grid-template-columns: repeat(auto-fit, minmax(150px, 1fr));
                            gap: 12px;
                        }
                        .info-item { padding: 14px; border-left: 3px solid #38bdf8; background: #172033; }
                        .label { color: #94a3b8; font-size: 12px; text-transform: uppercase; }
                        .value { margin-top: 6px; font-weight: 700; overflow-wrap: anywhere; }
                        @media (max-width: 600px) {
                            body { padding: 22px; }
                            h1 { font-size: 38px; }
                            .cards { grid-template-columns: 1fr; }
                        }
                    </style>
                </head>
                <body>
                    <main>
                        <section class="top">
                            <div>
                                <div class="eyebrow">Volunteer Platform</div>
                                <h1>Volunteer Platform</h1>
                                <h2>User Service</h2>
                            </div>
                            <div class="badge"><span class="dot"></span>RUNNING</div>
                        </section>
                        <p class="description">
                            This service manages users, volunteers, organizations, and user-related operations.
                        </p>
                        <section class="cards">
                            <a class="card" href="/q/health">
                                <strong>Health</strong>
                                <span>Check service liveness and readiness.</span>
                            </a>
                            <a class="card" href="/metrics">
                                <strong>Metrics</strong>
                                <span>View MicroProfile application metrics.</span>
                            </a>
                            <a class="card" href="/volunteers">
                                <strong>Volunteers API</strong>
                                <span>Open the volunteer endpoint.</span>
                            </a>
                            <a class="card" href="/organizations">
                                <strong>Organizations API</strong>
                                <span>Open the organization endpoint.</span>
                            </a>
                        </section>
                        <section class="info">
                            <h3>Service Info</h3>
                            <div class="info-grid">
                                <div class="info-item"><div class="label">Service name</div><div class="value">user-service</div></div>
                                <div class="info-item"><div class="label">Runtime</div><div class="value">Quarkus</div></div>
                                <div class="info-item"><div class="label">Port</div><div class="value">8081</div></div>
                                <div class="info-item"><div class="label">Status</div><div class="value">UP</div></div>
                            </div>
                        </section>
                    </main>
                </body>
                </html>
                """;
    }
}
