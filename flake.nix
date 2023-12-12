{
  description = "A very basic flake";

  inputs = {
    nixpkgs.url = "github:nixos/nixpkgs/nixpkgs-unstable";
    flake-utils.url = "github:numtide/flake-utils";
  };

  outputs = { self, nixpkgs, ... }@inputs:
    let supportedSystems = [ inputs.flake-utils.lib.system.aarch64-darwin ];
    in inputs.flake-utils.lib.eachSystem supportedSystems (system:
      let pkgs = import nixpkgs { inherit system; };
      in {
        devShells.default = let
          packages = with pkgs; [
            jre
            clojure
            dune_3
            ocaml
            ocamlPackages.utop
            ocamlPackages.merlin
            ocamlPackages.ocaml-lsp
            ocamlPackages.ocamlformat
          ];
        in pkgs.mkShell { buildInputs = packages; };
      });
}
